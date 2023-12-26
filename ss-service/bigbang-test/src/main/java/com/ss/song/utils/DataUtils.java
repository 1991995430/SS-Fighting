package com.ss.song.utils;

import com.alibaba.fastjson2.JSONObject;
import com.ss.song.enums.FlankerErrorType;
import com.ss.song.enums.SqlType;
import com.ss.song.meta.ColumnMeta;
import com.ss.song.meta.RelationMeta;
import com.ss.song.meta.TableMeta;
import com.ss.song.model.IndexedData;
import com.ss.song.model.Path;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Stream;

public final class DataUtils
{
	static Log log = LogFactory.getLog(DataUtils.class);

	/**
	 * 按照主键生成策略生成主键
	 *
	 * @param tableMeta
	 * @param data
	 * @return
	 */
	public static String generateKey(TableMeta tableMeta, Object data)
	{
		String key = null;
		switch (tableMeta.getKeyMeta().getGeneratorType())
		{
		case UUID:
			key = StringUtils.remove(UUID.randomUUID().toString(), '-');
			ReflectUtils.setFieldValue(data, tableMeta.getKeyMeta().getField(), key);
			break;
		case ASSIGNED:
			key = (String) ReflectUtils.getFieldValue(data, tableMeta.getKeyMeta().getField());
			break;
		}
		return key;
	}
	/**
	 * 将类属性类型转换为SQL类型
	 *
	 * @param dataType
	 * @return
	 */
	public static SqlType toSqlType(Class<?> dataType)
	{
		if (dataType == String.class || dataType.isEnum() || EnumSet.class.isAssignableFrom(dataType) || Collection.class.isAssignableFrom(dataType))
		{
			// 字符串,以及集合,数组,枚举等
			return SqlType.VARCHAR;
		}
		else if (dataType == Integer.class || dataType == int.class)
		{
			return SqlType.INT;
		}
		else if (dataType == Long.class || dataType == long.class)
		{
			return SqlType.BIGINT;
		}
		else if (dataType == Boolean.class || dataType == boolean.class)
		{
			return SqlType.BOOLEAN;
		}
		else if (dataType == BigDecimal.class)
		{
			return SqlType.DECIMAL;
		}
		else if (dataType == JSONObject.class)
		{
			return SqlType.TEXT;
		}
		else if (dataType == byte[].class || dataType == ByteBuffer.class)
		{
			return SqlType.BLOB;
		}
		throw new RuntimeException(FlankerErrorType.Query.UNKNOWN_FIELD_TYPE.message());
	}

	/**
	 * 返回全部的子孙对象(包含自己在内)
	 *
	 * @return
	 */
	public static Stream<TableData> dataStream(TableData rootTableData)
	{
		List<TableData> tableDataList = new ArrayList<>();
		tableDataList.add(rootTableData);
		addChildren(rootTableData, tableDataList);
		return tableDataList.stream();
	}

	@SuppressWarnings({ "rawtypes" })
	private static void addChildren(TableData tableData, List<TableData> tableDataList)
	{
		TableMeta[] childrenTableMetas = tableData.getTableMeta().getChildren();
		for (TableMeta childTableMeta : childrenTableMetas)
		{
			Object fieldValue = ReflectUtils.getFieldValue(tableData.getData(), childTableMeta.getParentTableField());
			if (fieldValue == null)
			{
				continue;
			}
			if (childTableMeta.isSingleton())
			{
				// 单例
				Object childObject = childTableMeta.getTableClass().cast(fieldValue);
				TableData childTableData = new TableData(childTableMeta, childObject);
				tableDataList.add(childTableData);
				addChildren(childTableData, tableDataList);// 递归调用
			}
			else
			{
				// 多实例
				List list = (List) fieldValue;
				if (CollectionUtils.isEmpty(list))
				{
					// 集合为空
					continue;
				}
				for (Object childObject : list)
				{
					TableData childTableData = new TableData(childTableMeta, childObject);
					tableDataList.add(childTableData);
					addChildren(childTableData, tableDataList);// 递归调用
				}
			}
		}
	}

	/**
	 * 按照递归方式添加子表数据
	 *
	 * @param indexedData
	 * @param key
	 * @param tableMeta
	 * @param insertQueryList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void insertChildren(IndexedData indexedData, String key, TableMeta tableMeta, List<Query> insertQueryList)
	{
		if (indexedData == null || tableMeta == null || tableMeta.isLeaf())
		{
			return;
		}
		for (TableMeta childTableMeta : tableMeta.getChildren())
		{
			Object fieldValue = ReflectUtils.getFieldValue(indexedData.getRaw(), childTableMeta.getParentTableField());
			if (fieldValue == null)
			{
				continue;
			}
			List childDataList = new ArrayList();
			if (childTableMeta.isSingleton())
			{
				// 单子表
				childDataList.add(fieldValue);
			}
			else
			{
				// 多子表
				List list = (List) fieldValue;
				if (CollectionUtils.isEmpty(list))
				{
					// 子表记录为空
					continue;
				}
				childDataList.addAll(list);
			}
			for (Object childData : childDataList)
			{
				if (childTableMeta.getParentObjectField() != null)
				{
					ReflectUtils.setFieldValue(childData, childTableMeta.getParentObjectField(), indexedData.getRaw());
				}
				IndexedData childIndexedData = new IndexedData(childData);
				childIndexedData.getPath().append(indexedData.getPath()).append(tableMeta.getTableClass(), key);
				String childKey = DataUtils.generateKey(childTableMeta, childData);
				Query insertQuery = QueryUtils.insertSql(childTableMeta, childIndexedData);
				insertQueryList.add(insertQuery);
				if (!childTableMeta.isLeaf())
				{
					// 有子表
					insertChildren(childIndexedData, childKey, childTableMeta, insertQueryList);// 递归调用
				}
			}
			childDataList.clear();
			childDataList = null;
		}
	}

	/**
	 * 递归加载数据
	 *
	 * @param data
	 * @param tableMeta
	 * @param childIndexedDataListMap
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void loadChildren(Object data, TableMeta tableMeta, Map<Class<?>, List<IndexedData>> childIndexedDataListMap)
	{
		if (data == null || tableMeta.isLeaf())
		{
			return;
		}
		String key = (String) ReflectUtils.getFieldValue(data, tableMeta.getKeyMeta().getField());// 获取当前表的KEY
		for (TableMeta childTableMeta : tableMeta.getChildren(false))
		{
			final Field parentField = childTableMeta.getParentTableField();// 所在父表属性
			// 首先将上级对象中的属性置空
			ReflectUtils.setFieldValue(data, parentField, null);
			List<IndexedData> childIndexedDataList = childIndexedDataListMap.get(childTableMeta.getTableClass());
			if (CollectionUtils.isEmpty(childIndexedDataList))
			{
				continue;
			}
			List<IndexedData> indexedDataList = new ArrayList<>();
			Iterator<IndexedData> it = childIndexedDataList.iterator();
			boolean singletonFound = false;// 是否已发现单例子对象
			while (it.hasNext())
			{
				IndexedData indexedData = it.next();
				String foreignKey = indexedData.getPath().getKey(tableMeta.getTableClass());
				if (StringUtils.equals(foreignKey, key))
				{
					// 上下级关系匹配
					if (childTableMeta.isSingleton())
					{
						// 单例模式
						if (!singletonFound)
						{
							ReflectUtils.setFieldValue(data, parentField, indexedData.getRaw());
							singletonFound = true;
							indexedDataList.add(indexedData);
						}
						else
						{
							// 异常情况,子对象出现多余1个实例的情况
							log.warn("more than 1 signleton dirty data found! table: " + tableMeta.getTableClass().getName() + ", foreignKey: " + foreignKey);
						}
					}
					else
					{
						// 添加到集合中
						List list = (List) ReflectUtils.getFieldValue(data, parentField);
						if (list == null)
						{
							list = new ArrayList<>();
							ReflectUtils.setFieldValue(data, parentField, list);
						}
						list.add(indexedData.getRaw());
						indexedDataList.add(indexedData);
					}
					// 从缓存中移除
					it.remove();
				}
			}
			for (IndexedData indexedData : indexedDataList)
			{
				// 递归调用
				if (childTableMeta.getParentObjectField() != null)
				{
					// 存在父对象属性
					ReflectUtils.setFieldValue(indexedData.getRaw(), childTableMeta.getParentObjectField(), data);
				}
				loadChildren(indexedData.getRaw(), childTableMeta, childIndexedDataListMap);
			}
			if (indexedDataList.size() > 1)
			{
				// 子对象集合
				Object raw = indexedDataList.get(0).getRaw();
				if (raw instanceof Comparable)
				{
					List list = (List) ReflectUtils.getFieldValue(data, parentField);
					Collections.sort(list);// 自动排序
				}
			}
			indexedDataList.clear();
			indexedDataList = null;
		}
	}

	/**
	 * 覆盖更新
	 *
	 * @param tableMeta
	 * @param path
	 * @param oldData
	 * @param newData
	 * @param queryList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void overwrite(TableMeta tableMeta, Path path, Object oldData, Object newData, List<Query> queryList)
	{
		if (oldData == newData)
		{

		}
		String key = path.getKey(tableMeta.getTableClass());
		List<Updated> updatedList = DataUtils.overwrite(tableMeta, oldData, newData);
		if (!CollectionUtils.isEmpty(updatedList))
		{
			// 添加update语句
			queryList.add(QueryUtils.updateSql(tableMeta, key, updatedList));
		}
		// 检查关系属性
		if (tableMeta.existsRelation())
		{
			for (RelationMeta relationMeta : tableMeta.getRelationMetas())
			{
				Set<String> newKeySet = (Set<String>) ReflectUtils.getFieldValue(newData, relationMeta.getField());
				if (newKeySet == null)
				{
					// 属性为NULL予以忽略
					continue;
				}
				Set<String> oldKeySet = (Set<String>) ReflectUtils.getFieldValue(oldData, relationMeta.getField());
				if (oldKeySet == null || !oldKeySet.equals(newKeySet))
				{
					// 新旧关联集合发生改变
					ReflectUtils.setFieldValue(oldData, relationMeta.getField(), newKeySet);
					// 先清空后添加
					queryList.add(QueryUtils.deleteSql(relationMeta.getRelationTable(), tableMeta.getTableClass(), key));
					queryList.addAll(QueryUtils.insertSql(relationMeta.getRelationTable(), tableMeta.getTableClass(), key, newKeySet));
				}
			}
		}
		if (tableMeta.isLeaf())
		{
			// 无子表
			return;
		}
		for (TableMeta childTableMeta : tableMeta.getChildren())
		{
			Object newFieldValue = ReflectUtils.getFieldValue(newData, childTableMeta.getParentTableField());
			if (newFieldValue == null)
			{
				// 新对象子表对象为null,则予以忽略
				continue;
			}
			Class<?> childTableClass = childTableMeta.getTableClass();
			Object oldFieldValue = ReflectUtils.getFieldValue(oldData, childTableMeta.getParentTableField());
			if (childTableMeta.isSingleton())
			{
				// 单例
				Object newChildData = childTableClass.cast(newFieldValue);
				Object oldChildData = childTableClass.cast(oldFieldValue);
				if (oldChildData == null)
				{
					// 原值不存在,需要添加
					String newChildKey = DataUtils.generateKey(childTableMeta, newChildData);
					IndexedData newIndexedData = new IndexedData(newChildData, path);
					queryList.add(QueryUtils.insertSql(childTableMeta, newIndexedData));
					insertChildren(newIndexedData, newChildKey, childTableMeta, queryList);// 递归调用
					// 将完成持久化的对象设置到老对象属性
					ReflectUtils.setFieldValue(oldData, childTableMeta.getParentTableField(), newIndexedData.getRaw());
					if (childTableMeta.getParentObjectField() != null)
					{
						ReflectUtils.setFieldValue(newIndexedData.getRaw(), childTableMeta.getParentObjectField(), oldData);
					}
				}
				else
				{
					// 原值存在,当主键相等时进行覆盖更新
					String oldChildKey = (String) ReflectUtils.getFieldValue(oldChildData, childTableMeta.getKeyMeta().getField());
					String newChildKey = (String) ReflectUtils.getFieldValue(newChildData, childTableMeta.getKeyMeta().getField());
					if (StringUtils.equals(oldChildKey, newChildKey))
					{
						// 新老主键相等,满足覆盖条件
						path.append(childTableMeta.getTableClass(), oldChildKey);
						overwrite(childTableMeta, path, oldChildData, newChildData, queryList);// 递归调用
					}
				}
			}
			else
			{
				// 多实例
				List oldList = (List) oldFieldValue;// 老子表集合
				Map<String, Object> oldChildDataMap = new HashMap<>();
				if (oldList == null)
				{
					// 原值为null,进行初始化
					oldList = new ArrayList<>();
					ReflectUtils.setFieldValue(oldData, childTableMeta.getParentTableField(), oldList);
				}
				else if (!oldList.isEmpty())
				{
					// 原值集合不为空
					for (Object oldChildData : oldList)
					{
						String oldChildKey = (String) ReflectUtils.getFieldValue(oldChildData, childTableMeta.getKeyMeta().getField());
						if (!StringUtils.isEmpty(oldChildKey))
						{
							oldChildDataMap.put(oldChildKey, oldChildData);
						}
						else
						{
							// 异常情况,原值集合中出现主键为空的情况
							log.warn("key is null or empty! Path: " + path + ", childTable: " + childTableMeta.getName());
						}
					}
				}
				List newList = (List) newFieldValue;// 新子表集合
				List<Object> tempList = new ArrayList<>();
				for (Object newChildData : newList)
				{
					String newChildKey = (String) ReflectUtils.getFieldValue(newChildData, childTableMeta.getKeyMeta().getField());
					if (StringUtils.isEmpty(newChildKey))
					{
						// 主键为空,判定为新子表记录
						newChildKey = DataUtils.generateKey(childTableMeta, newChildData);
						IndexedData newIndexedData = new IndexedData(newChildData, path);
						queryList.add(QueryUtils.insertSql(childTableMeta, newIndexedData));// 添加insert语句
						insertChildren(newIndexedData, newChildKey, childTableMeta, queryList);// 递归调用
						tempList.add(newIndexedData.getRaw());// 添加到新记录临时集合中
						if (childTableMeta.getParentObjectField() != null)
						{
							// 存在父对象属性
							ReflectUtils.setFieldValue(newIndexedData.getRaw(), childTableMeta.getParentObjectField(), oldData);
						}
					}
					else
					{
						// 新子表主键非空
						Object oldChildData = oldChildDataMap.get(newChildKey);
						if (oldChildData == null)
						{
							// 原对象不存在,则该记录可能最近被删除,故予以忽视
							continue;
						}
						path.append(childTableMeta.getTableClass(), newChildKey);
						overwrite(childTableMeta, path, oldChildData, newChildData, queryList);// 递归调用
						oldChildDataMap.remove(newChildKey);
						tempList.add(oldChildData);
					}
				}
				if (!oldChildDataMap.isEmpty())
				{
					// 剩余的对象需要予以删除
					for (String oldChildKey : oldChildDataMap.keySet())
					{
						String sql = "DELETE FROM " + childTableMeta.getName() + " WHERE " + childTableMeta.getKeyMeta().getName() + "=?";
						queryList.add(new Query(sql, oldChildKey));// 删除语句
						if (!childTableMeta.isLeaf())
						{
							// 删除子孙子表记录
							for (TableMeta grandsonTableMeta : childTableMeta.getChildren(true))
							{
								sql = "DELETE FROM " + grandsonTableMeta.getName() + " WHERE " + childTableMeta.foreignKeyName() + "=?";
								queryList.add(new Query(sql, oldChildKey));// 删除语句
							}
						}
					}
					oldChildDataMap.clear();
					oldChildDataMap = null;
				}
				oldList.clear();
				oldList.addAll(tempList);
			}
		}
	}

	/**
	 * 复制data对象中Column定义的属性到cachedData中
	 *
	 * @param tableMeta
	 */
	public static void copyColumns(TableMeta tableMeta, Object srcData, Object destData)
	{
		if (destData == null || srcData == null)
		{
			throw new IllegalArgumentException("oldData or newData cannot null!");
		}
		if (destData.getClass() != tableMeta.getTableClass() || srcData.getClass() != tableMeta.getTableClass())
		{
			throw new IllegalArgumentException("oldData or newData class is not " + tableMeta.getTableClass());
		}
		for (ColumnMeta column : tableMeta.getColumns())
		{
			Field field = column.getField();
			Object newValue = ReflectUtils.getFieldValue(srcData, field);
			if (newValue == null)
			{
				// 新属性为NULL的予以忽略
				continue;
			}
			Object oldValue = ReflectUtils.getFieldValue(destData, field);
			if (oldValue == null)
			{
				// 原属性为NULL,则必然改变
				ReflectUtils.setFieldValue(destData, field, newValue);
				continue;
			}
			boolean equals = true;
			switch (column.getSqlType())
			{
			case INT:
				equals = ((Integer) oldValue).compareTo((Integer) newValue) == 0;
				break;
			case BIGINT:
				equals = ((Long) oldValue).compareTo((Long) newValue) == 0;
				break;
			case DECIMAL:
				equals = ((BigDecimal) oldValue).compareTo((BigDecimal) newValue) == 0;
				break;
			case BOOLEAN:
				equals = ((Boolean) oldValue).compareTo((Boolean) newValue) == 0;
				break;
			default:
				equals = oldValue.equals(newValue);
				break;
			}
			if (!equals)
			{
				ReflectUtils.setFieldValue(destData, field, newValue);
			}
		}
	}

	/**
	 * 不考虑主键/外键和只读字段,其余的字段进行对比,并返回发生改变的字段集合
	 *
	 * @param tableMeta
	 * @param oldData
	 * @param newData
	 * @return
	 */
	private static List<Updated> overwrite(TableMeta tableMeta, Object oldData, Object newData)
	{
		if (oldData == null || newData == null)
		{
			throw new IllegalArgumentException("oldData or newData cannot null!");
		}
		if (oldData.getClass() != tableMeta.getTableClass() || newData.getClass() != tableMeta.getTableClass())
		{
			throw new IllegalArgumentException("oldData or newData class is not " + tableMeta.getTableClass());
		}
		List<Updated> updatedList = new ArrayList<>();
		for (ColumnMeta column : tableMeta.getColumns())
		{
			if (column.isReadonly())
			{
				// 只读字段予以忽略
				continue;
			}
			Field field = column.getField();
			Object newValue = ReflectUtils.getFieldValue(newData, field);
			if (newValue == null)
			{
				// 新属性为NULL的予以忽略
				continue;
			}
			Object oldValue = ReflectUtils.getFieldValue(oldData, field);
			if (oldValue == null)
			{
				// 原属性为NULL,则必然改变
				ReflectUtils.setFieldValue(oldData, field, newValue);
				updatedList.add(new Updated(column, oldValue, newValue));
				continue;
			}
			boolean equals = true;
			switch (column.getSqlType())
			{
			case INT:
				equals = ((Integer) oldValue).compareTo((Integer) newValue) == 0;
				break;
			case BIGINT:
				equals = ((Long) oldValue).compareTo((Long) newValue) == 0;
				break;
			case DECIMAL:
				equals = ((BigDecimal) oldValue).compareTo((BigDecimal) newValue) == 0;
				break;
			case BOOLEAN:
				equals = ((Boolean) oldValue).compareTo((Boolean) newValue) == 0;
				break;
			default:
				equals = oldValue.equals(newValue);
				break;
			}
			if (!equals)
			{
				ReflectUtils.setFieldValue(oldData, field, newValue);
				updatedList.add(new Updated(column, oldValue, newValue));
			}
		}
		return updatedList;
	}

	private DataUtils()
	{
	}
}
