package com.ss.song.dao;

import com.ss.song.meta.TableMeta;
import com.ss.song.model.IndexedData;
import com.ss.song.service.FlankerMetaService;
import com.ss.song.service.RelationService;
import com.ss.song.utils.DataUtils;
import com.ss.song.utils.Query;
import com.ss.song.utils.QueryUtils;
import com.ss.song.utils.ReflectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class RootDataDao
{
	@Resource
	private FlankerMetaService flankerMetaService;
	@Resource
	private FlankerJdbcTemplate flankerJdbcTemplate;
	@Resource
	private RelationService relationService;

	Log log = LogFactory.getLog(getClass());

	/**
	 * 完整加载一个根对像
	 *
	 * @param tableClass
	 * @param key
	 * @return
	 */
	public <T> T load(Class<T> tableClass, String key)
	{
		TableMeta rootTableMeta = flankerMetaService.getRootTableMeta(tableClass);
		// 必须确保记录的唯一性
		IndexedData rootIndexedData = flankerJdbcTemplate.queryForFirstObject(rootTableMeta, "SELECT * FROM " + rootTableMeta.getName() + " WHERE " + rootTableMeta.getKeyMeta().getName() + "=?", key);
		if (rootIndexedData == null)
		{
			// 根对象不存在
			return null;
		}
		if (!rootTableMeta.isLeaf())
		{
			// 存在子孙对象
			Map<Class<?>, List<IndexedData>> childIndexedDataListMap = new HashMap<>();
			for (TableMeta childTableMeta : rootTableMeta.getChildren(true))
			{
				List<IndexedData> childIndexedDataList = flankerJdbcTemplate.queryForList(childTableMeta, "SELECT * FROM " + childTableMeta.getName() + " WHERE " + rootTableMeta.foreignKeyName() + "=?", key);
				childIndexedDataListMap.put(childTableMeta.getTableClass(), childIndexedDataList);
			}
			// 以递归方式从根节点开始加载子表记录
			DataUtils.loadChildren(rootIndexedData.getRaw(), rootTableMeta, childIndexedDataListMap);
			childIndexedDataListMap.clear();
			childIndexedDataListMap = null;
		}
		// 加载关系
		relationService.loadRelation(rootTableMeta, rootIndexedData.getRaw(), true);
		return tableClass.cast(rootIndexedData.getRaw());
	}

	/**
	 * 加载执行类型的全部根对像
	 *
	 * @param <T>
	 * @param tableClass
	 * @return
	 */
	public <T> List<T> loadAll(Class<T> tableClass)
	{
		TableMeta rootTableMeta = flankerMetaService.getRootTableMeta(tableClass);
		List<IndexedData> rootIndexedDataList = flankerJdbcTemplate.queryForList(rootTableMeta, "SELECT * FROM " + rootTableMeta.getName(), new Object[0]);
		if (!rootTableMeta.isLeaf() && !CollectionUtils.isEmpty(rootIndexedDataList))
		{
			// 非树叶对象
			Map<Class<?>, List<IndexedData>> childIndexedDataListMap = new HashMap<>();
			for (TableMeta childTableMeta : rootTableMeta.getChildren(true))
			{
				List<IndexedData> childIndexedDataList = flankerJdbcTemplate.queryForList(childTableMeta, "SELECT * FROM " + childTableMeta.getName(), new Object[0]);
				childIndexedDataListMap.put(childTableMeta.getTableClass(), childIndexedDataList);
			}
			for (IndexedData rootIndexedData : rootIndexedDataList)
			{
				// 以递归方式从根节点开始加载子表记录
				DataUtils.loadChildren(rootIndexedData.getRaw(), rootTableMeta, childIndexedDataListMap);
			}
			childIndexedDataListMap.clear();
			childIndexedDataListMap = null;
		}
		for (IndexedData rootIndexedData : rootIndexedDataList)
		{
			// 设置关联字段
			relationService.loadRelation(rootTableMeta, rootIndexedData.getRaw(), true);
		}
		return rootIndexedDataList.stream().map(indexedData -> tableClass.cast(indexedData.getRaw())).collect(Collectors.toList());
	}

	/**
	 * 添加一个根对象,并返回ID
	 *
	 */
	public String insert(Object rootData)
	{
		TableMeta rootTableMeta = flankerMetaService.getRootTableMeta(rootData.getClass());
		String rootKey = DataUtils.generateKey(rootTableMeta, rootData);
		IndexedData rootIndexedData = new IndexedData(rootData);
		Query insertQuery = QueryUtils.insertSql(rootTableMeta, rootIndexedData);
		List<Query> insertQueryList = new ArrayList<>();
		insertQueryList.add(insertQuery);
		if (!rootTableMeta.isLeaf())
		{
			DataUtils.insertChildren(rootIndexedData, rootKey, rootTableMeta, insertQueryList);
		}
		// 插入关联关系
		relationService.insertRelation(rootTableMeta, rootData, true, insertQueryList);
		flankerJdbcTemplate.executeUpdate(insertQueryList);
		return rootKey;
	}

	/**
	 * 删除一个根对像
	 *
	 */
	public void delete(Object rootData)
	{
		TableMeta rootTableMeta = flankerMetaService.getRootTableMeta(rootData.getClass());
		String rootKey = (String) ReflectUtils.getFieldValue(rootData, rootTableMeta.getKeyMeta().getField());
		List<Query> deleteQueryList = new ArrayList<>();
		// 删除各级子表
		for (TableMeta childTableMeta : rootTableMeta.getChildren(true))
		{
			deleteQueryList.add(new Query("DELETE FROM " + childTableMeta.getName() + " WHERE " + rootTableMeta.foreignKeyName() + "=?", rootKey));
		}
		// 删除关联表
		relationService.deleteRelation(rootTableMeta, rootData, true, deleteQueryList);
		// 删除自己
		deleteQueryList.add(new Query("DELETE FROM " + rootTableMeta.getName() + " WHERE " + rootTableMeta.getKeyMeta().getName() + "=?", rootKey));
		flankerJdbcTemplate.executeUpdate(deleteQueryList);
	}

}
