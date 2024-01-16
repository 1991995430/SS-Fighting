package com.ss.song.service.impl;

import com.ss.song.dao.FlankerJdbcTemplate;
import com.ss.song.enums.FetchMode;
import com.ss.song.enums.FlankerErrorType;
import com.ss.song.meta.TableMeta;
import com.ss.song.model.Indexed;
import com.ss.song.model.IndexedData;
import com.ss.song.model.Path;
import com.ss.song.model.TreeNode;
import com.ss.song.service.*;
import com.ss.song.utils.DataUtils;
import com.ss.song.utils.Query;
import com.ss.song.utils.QueryUtils;
import com.ss.song.utils.ReflectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FlankerTemplateImpl implements FlankerTemplate {
    @Resource
    private FlankerMetaService flankerMetaService;
    @Resource
    private FlankerJdbcTemplate flankerJdbcTemplate;
    @Resource
    private FlankerCache flankerCache;
    @Resource
    private FlankerRendererService flankerRendererService;
    @Resource
    private RelationService relationService;

    Log log = LogFactory.getLog(getClass());

    @Override
    public <T> T get(Class<T> tableClass, String key) {
        TableMeta tableMeta = flankerMetaService.getTableMeta(tableClass);
        Path path = this.flankerJdbcTemplate.getPath(tableMeta, key);
        return tableClass.cast(flankerCache.find(path));
    }

    @Override
    public void insertRoot(Object rootData) {
        if (rootData == null) {
            throw new IllegalArgumentException("rootData cannot null!");
        }
        flankerCache.insertRoot(rootData);
        flankerRendererService.render(rootData);
    }

    @Override
    public void insert(Path path, Object data) {
        TableMeta tableMeta = flankerMetaService.getTableMeta(data.getClass());
        if (tableMeta.isRoot()) {
            // 根对像
            this.insertRoot(data);
            return;
        }
        Object cachedData = flankerCache.find(path);// 获取上一级缓存实例
        if (cachedData == null) {
            // 上一级数据对象不存在
            //throw new StandardException(FlankerErrorType.Query.PARENT_DATA_NOT_EXISTS, path);
        }
        List<Query> insertQueryList = new ArrayList<>();
        try {
            synchronized (cachedData) {
                String key = DataUtils.generateKey(tableMeta, data);// 生成主键
                IndexedData indexedData = new IndexedData(data, path);
                Query insertQuery = QueryUtils.insertSql(tableMeta, indexedData);
                insertQueryList.add(insertQuery);
                if (!tableMeta.isLeaf()) {
                    DataUtils.insertChildren(indexedData, key, tableMeta, insertQueryList);
                }
                // 插入关联关系
                relationService.insertRelation(tableMeta, data, true, insertQueryList);
                flankerJdbcTemplate.executeUpdate(insertQueryList);
                //FlankerSession.current().append(path);// 事务后局部刷新
            }
        } finally {
            insertQueryList.clear();
            insertQueryList = null;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void delete(Class<?> tableClass, String key) {
        TableMeta tableMeta = flankerMetaService.getTableMeta(tableClass);
        if (tableMeta.isRoot()) {
            // 删除根节点
            flankerCache.deleteRoot(tableClass, key);
            //FlankerSession.current().append(new Path(tableClass, key));
        } else {
            // 非根对象
            Path path = this.flankerJdbcTemplate.getPath(tableMeta, key);
            Object cachedData = flankerCache.find(path);
            Path parentPath = path.getParent();
            Object cachedParentData = flankerCache.find(parentPath);// 获取上一级缓存对象
            if (cachedParentData == null) {
                // 上一级数据对象不存在
                //throw new StandardException(FlankerErrorType.Query.PARENT_DATA_NOT_EXISTS, parentPath);
            }
            List<Query> deleteQueryList = new ArrayList<>();
            deleteQueryList.add(new Query("DELETE FROM " + tableMeta.getName() + " WHERE " + tableMeta.getKeyMeta().getName() + "=?", key));
            if (!tableMeta.isLeaf()) {
                // 删除子孙子表
                for (TableMeta childTableMeta : tableMeta.getChildren(true)) {
                    deleteQueryList.add(new Query("DELETE FROM " + childTableMeta.getName() + " WHERE " + tableMeta.foreignKeyName() + "=?", key));
                }
            }
            if (cachedData != null) {
                // 删除关系
                relationService.deleteRelation(tableMeta, cachedData, true, deleteQueryList);
            }
            try {
                synchronized (cachedParentData) {
                    // 批量删除操作
                    flankerJdbcTemplate.executeUpdate(deleteQueryList);
                    if (tableMeta.isSingleton()) {
                        // 单例,直接将属性置空
                        ReflectUtils.setFieldValue(cachedParentData, tableMeta.getParentTableField(), null);
                    } else {
                        // 多实例
                        List list = (List) ReflectUtils.getFieldValue(cachedParentData, tableMeta.getParentTableField());
                        if (!CollectionUtils.isEmpty(list)) {
                            Iterator<Object> it = list.iterator();
                            while (it.hasNext()) {
                                Object childData = it.next();
                                String childKey = (String) ReflectUtils.getFieldValue(childData, tableMeta.getKeyMeta().getField());
                                if (StringUtils.equals(key, childKey)) {
                                    // 找到需要删除的对象
                                    it.remove();
                                    break;
                                }
                            }
                        }
                    }
                    //FlankerSession.current().append(parentPath);
                }
            } finally {
                deleteQueryList.clear();
                deleteQueryList = null;
            }

        }
    }

    @Override
    public String getKey(Object data) {
        if (data == null) {
            throw new IllegalArgumentException("data cannot null!");
        }
        TableMeta tableMeta = this.flankerMetaService.getTableMeta(data.getClass());
        return (String) ReflectUtils.getFieldValue(data, tableMeta.getKeyMeta().getField());
    }

    @Override
    public void setKey(Object data, String key) {
        if (data == null) {
            throw new IllegalArgumentException("data cannot null!");
        }
        TableMeta tableMeta = this.flankerMetaService.getTableMeta(data.getClass());
        ReflectUtils.setFieldValue(data, tableMeta.getKeyMeta().getField(), key);
    }

    @Override
    public void overwrite(Object data) {
        if (data == null) {
            throw new IllegalArgumentException("data cannot null!");
        }
        TableMeta tableMeta = this.flankerMetaService.getTableMeta(data.getClass());
        String key = (String) ReflectUtils.getFieldValue(data, tableMeta.getKeyMeta().getField());
        if (tableMeta.isRoot() && StringUtils.isEmpty(key)) {
            // 根对象,且KEY为空,则直接添加
            this.insertRoot(data);
            return;
        }
        Path path = this.flankerJdbcTemplate.getPath(tableMeta, key);// 获取该对象所在路径
        Object cachedData = flankerCache.find(path);// 获取缓存数据
        if (cachedData == null) {
            // 缓存对象不存在
            //throw new StandardException(FlankerErrorType.Query.PATH_DATA_NOT_EXISTS, path);
            System.out.println();
        }
        if (cachedData == data) {
            // 不可使用已缓存的对象进行覆盖操作
            //throw new StandardException(FlankerErrorType.Query.CACHED_CANNOT_OVERWRITE, path);
            System.out.println();
        }
        List<Query> updateQueryList = new ArrayList<>();
        try {
            synchronized (cachedData) {
                // 进行覆盖写操作
                DataUtils.overwrite(tableMeta, new Path(path), cachedData, data, updateQueryList);
                if (!updateQueryList.isEmpty()) {
                    // 批量更新(可能涉及增删改)
                    flankerJdbcTemplate.executeUpdate(updateQueryList);
                    //FlankerSession.current().append(path);
                }
            }
        } finally {
            updateQueryList.clear();
            updateQueryList = null;
        }
    }

    /**
     * 从DB局部刷新对象(及其子孙记录)
     */
    @Override
    public void reload(Path path) {
        Object cachedData = flankerCache.find(path);
        if (cachedData == null) {
            if (path.size() == 1) {
                // 根节点路径,需要判断DB中是否存在该对象
                Indexed rootIndexed = path.first();
                cachedData = this.flankerCache.reloadRoot(rootIndexed.getTableClass(), rootIndexed.getKey());
            }
            if (cachedData == null) {
                // 需要重加载的对象不存在
                //throw new StandardException(FlankerErrorType.Query.PATH_DATA_NOT_EXISTS, path);
            }
        } else {
            Indexed indexed = path.last();
            TableMeta tableMeta = this.flankerMetaService.getTableMeta(indexed.getTableClass());
            IndexedData indexedData = flankerJdbcTemplate.queryForObject(tableMeta, "SELECT * FROM " + tableMeta.getName() + " WHERE " + tableMeta.getKeyMeta().getName() + "=?", indexed.getKey());
            DataUtils.copyColumns(tableMeta, indexedData.getRaw(), cachedData);// 属性复制到缓存数据中
            if (!tableMeta.isLeaf()) {
                // 将子孙表中的数据全部予以加载
                Map<Class<?>, List<IndexedData>> childIndexedDataListMap = new HashMap<>();
                for (TableMeta childTableMeta : tableMeta.getChildren(true)) {
                    List<IndexedData> childIndexedDataList = flankerJdbcTemplate.queryForList(childTableMeta, "SELECT * FROM " + childTableMeta.getName() + " WHERE " + tableMeta.foreignKeyName() + "=?", indexed.getKey());
                    childIndexedDataListMap.put(childTableMeta.getTableClass(), childIndexedDataList);
                }
                // 以递归方式从根节点开始加载子表记录
                DataUtils.loadChildren(cachedData, tableMeta, childIndexedDataListMap);
                childIndexedDataListMap.clear();
                childIndexedDataListMap = null;
            }
            // 重新加载关系
            relationService.loadRelation(tableMeta, cachedData, true);
        }
        // 渲染对象
        flankerRendererService.render(cachedData);
    }

    @Override
    public <T> Stream<T> queryForStream(Class<T> tableClass, FlankerFilter<T> filter, Comparator<T> comparator) {
        this.flankerMetaService.getRootTableMeta(tableClass);// 必须是根对像
        Stream<T> stream = flankerCache.getRootStream(tableClass).filter(data -> filter == null || filter.filter(data));
        if (comparator != null) {
            // 按照指定规则进行排序
            return stream.sorted(comparator);
        } else if (Comparable.class.isAssignableFrom(tableClass)) {
            // 默认的对象排序
            return stream.sorted();
        } else {
            // 不排序
            return stream;
        }
    }

    @Override
    public <T> Stream<T> queryForStream(Class<T> tableClass, Comparator<T> comparator) {
        this.flankerMetaService.getRootTableMeta(tableClass);// 必须是根对像
        Stream<T> stream = flankerCache.getRootStream(tableClass);
        if (comparator != null) {
            // 按照指定规则进行排序
            return stream.sorted(comparator);
        } else if (Comparable.class.isAssignableFrom(tableClass)) {
            // 默认的对象排序
            return stream.sorted();
        } else {
            // 不排序
            return stream;
        }
    }

    @Override
    public <T> T queryForObject(Class<T> tableClass, FlankerFilter<T> filter) {
        List<T> list = this.queryForStream(tableClass, filter, null).collect(Collectors.toList());
        if (list.isEmpty()) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new RuntimeException(FlankerErrorType.Query.DATA_NOT_UNIQUE.message());
        }
    }

	/*@Override
	public <T> PagingResult<T> queryForPagingResult(Class<T> tableClass, FlankerFilter<T> filter, Comparator<T> comparator, int start, int limit)
	{
		List<T> list = this.queryForStream(tableClass, filter, comparator).collect(Collectors.toList());
		return PagingResult.of(list, start, limit);
	}*/

    @Override
    public <T extends TreeNode<T>> List<T> getTreeList(Class<T> tableClass) {
        List<T> rootList = new ArrayList<>();
        List<T> dataList = this.queryForStream(tableClass, null, null, FetchMode.CLONED).collect(Collectors.toList());
        Stack<T> stack = new Stack<>();
        dataList.stream().filter(data -> data.isRoot()).forEach(root ->
        {
            stack.push(root);
            rootList.add(root);
        });
        while (!stack.isEmpty()) {
            T node = stack.pop();
            dataList.stream().filter(data -> StringUtils.equals(node.getId(), data.getParentId())).forEach(child ->
            {
                node.addChild(child);
                stack.push(child);
            });
        }
        return rootList;
    }

}
