package com.ss.song.utils;

import com.ss.song.meta.ColumnMeta;
import com.ss.song.meta.TableMeta;
import com.ss.song.model.IndexedData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class QueryUtils
{
	public static final String MYSQL_ENGINE = "InnoDB";
	public static final String MYSQL_CHARSET = "utf8mb4";

	/**
	 * 创建常规表
	 *
	 * @param tableMeta
	 * @return
	 */
	public static Query createSql(TableMeta tableMeta)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("CREATE TABLE IF NOT EXISTS ").append(tableMeta.getName()).append(" (");
		List<String> columnSqlList = new ArrayList<>();
		// 主键
		columnSqlList.add(tableMeta.getKeyMeta().sqlString());
		for (TableMeta parent : tableMeta.getParents())
		{
			// 外键
			columnSqlList.add(parent.foreignKeyName() + " " + parent.foreignKeySqlType() + " NOT NULL");
		}
		for (ColumnMeta column : tableMeta.getColumns())
		{
			// 普通字段
			columnSqlList.add(column.sqlString());
		}
		buffer.append(StringUtils.join(columnSqlList, ", ")).append(") ENGINE=").append(MYSQL_ENGINE).append(" DEFAULT CHARSET=").append(MYSQL_CHARSET);
		return new Query(buffer.toString());
	}

	/**
	 * 创建关系表
	 *
	 * @param relationTable
	 * @return
	 */
	public static Query createSql(RelationTable relationTable)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("CREATE TABLE IF NOT EXISTS ").append(relationTable.getName()).append(" (");
		buffer.append(relationTable.getLeftColumnName()).append(" VARCHAR(32), ");
		buffer.append(relationTable.getRightColumnName()).append(" VARCHAR(32), ");
		buffer.append("PRIMARY KEY (").append(StringUtils.join(relationTable.getColumnNames(), ",")).append(")");
		buffer.append(") ENGINE=").append(MYSQL_ENGINE).append(" DEFAULT CHARSET=").append(MYSQL_CHARSET);
		return new Query(buffer.toString());
	}

	/**
	 * 更新语句
	 *
	 * @param tableMeta
	 * @param data
	 * @return
	 */
	public static Query updateSql(TableMeta tableMeta, Object data)
	{
		if (data == null || data.getClass() != tableMeta.getTableClass())
		{
			throw new IllegalArgumentException("invalid data! " + data);
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE ").append(tableMeta.getName()).append(" SET ");
		List<String> sqlList = new ArrayList<>();
		Query query = new Query();
		for (ColumnMeta column : tableMeta.getColumns())
		{
			if (column.isReadonly())
			{
				// 只读字段不予更新
				continue;
			}
			sqlList.add(column.getName() + "=?");
			Object value = ReflectUtils.getFieldValue(data, column.getField());
			query.append(column, value);
		}
		buffer.append(StringUtils.join(sqlList, ", ")).append(" WHERE ").append(tableMeta.getKeyMeta().getName()).append("=?");
		String key = (String) ReflectUtils.getFieldValue(data, tableMeta.getKeyMeta().getField());
		return query.appendKey(key).setSql(buffer.toString());
	}

	/**
	 * 删除语句
	 *
	 * @param tableMeta
	 * @param data
	 * @return
	 */
	public static Query deleteSql(TableMeta tableMeta, Object data)
	{
		if (data == null || data.getClass() != tableMeta.getTableClass())
		{
			throw new IllegalArgumentException("invalid data! " + data);
		}
		String key = (String) ReflectUtils.getFieldValue(data, tableMeta.getKeyMeta().getField());
		StringBuffer buffer = new StringBuffer();
		buffer.append("DELETE ").append(tableMeta.getName()).append(" FROM ").append(tableMeta.getName()).append(" WHERE ").append(tableMeta.getKeyMeta().getName()).append("=?");
		return new Query(buffer.toString(), key);
	}

	public static List<Query> insertSql(RelationTable relationTable, Class<?> tableClass, String key, Set<String> otherKeySet)
	{
		List<Query> queryList = new ArrayList<>();
		for (String otherKey : otherKeySet)
		{
			queryList.add(insertSql(relationTable, tableClass, key, otherKey));
		}
		return queryList;
	}

	/**
	 * 插入一条关系
	 *
	 * @return
	 */
	public static Query insertSql(RelationTable relationTable, Class<?> tableClass, String key, String otherKey)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO ").append(relationTable.getName()).append("(").append(StringUtils.join(relationTable.getColumnNames(), ",")).append(") VALUES (?,?)");
		String sql = buffer.toString();
		if (tableClass == relationTable.getLeft())
		{
			return new Query(sql, key, otherKey);
		}
		else
		{
			return new Query(sql, otherKey, key);
		}
	}

	/**
	 * 删除一条关系
	 *
	 * @return
	 */
	public static Query deleteSql(RelationTable relationTable, String leftKey, String rightKey)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("DELETE FROM ").append(relationTable.getName()).append(" WHERE ").append(relationTable.getLeftColumnName()).append("=? AND ").append(relationTable.getRightColumnName()).append("=?");
		return new Query(buffer.toString(), leftKey, rightKey);
	}

	public static Query deleteSql(RelationTable relationTable, Class<?> tableClass, String key)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("DELETE FROM ").append(relationTable.getName()).append(" WHERE ").append(relationTable.toColumnName(tableClass)).append("=?");
		return new Query(buffer.toString(), key);
	}

	/**
	 * 插入语句
	 *
	 * @param tableMeta
	 * @return
	 */
	public static Query insertSql(TableMeta tableMeta, IndexedData indexedData)
	{
		if (indexedData == null || indexedData.getRaw().getClass() != tableMeta.getTableClass())
		{
			throw new IllegalArgumentException("invalid data! " + indexedData);
		}
		List<String> nameList = new ArrayList<>();
		List<String> paramList = new ArrayList<>();
		Query query = new Query();
		String key = (String) ReflectUtils.getFieldValue(indexedData.getRaw(), tableMeta.getKeyMeta().getField());
		// 设置主键
		nameList.add(tableMeta.getKeyMeta().getName());
		paramList.add("?");
		query.appendKey(key);
		for (TableMeta parent : tableMeta.getParents())
		{
			// 设置外键
			nameList.add(parent.foreignKeyName());
			paramList.add("?");
			if (!indexedData.getPath().containsTableClass(parent.getTableClass()))
			{

			}
			String foreignKey = indexedData.getPath().getKey(parent.getTableClass());
			query.appendKey(foreignKey);
		}
		for (ColumnMeta column : tableMeta.getColumns())
		{
			// 设置普通字段
			nameList.add(column.getName());
			paramList.add("?");
			Object value = ReflectUtils.getFieldValue(indexedData.getRaw(), column.getField());
			query.append(column, value);
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO ").append(tableMeta.getName()).append(" (").append(StringUtils.join(nameList, ",")).append(") VALUES (").append(StringUtils.join(paramList, ",")).append(")");
		query.setSql(buffer.toString());
		return query;
	}

	public static Query updateSql(TableMeta tableMeta, String key, List<Updated> updatedList)
	{
		if (CollectionUtils.isEmpty(updatedList))
		{
			return null;
		}
		List<String> sqlList = updatedList.stream().map(updated -> updated.getColumnMeta().getName() + "=?").collect(Collectors.toList());
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE ").append(tableMeta.getName()).append(" SET ").append(StringUtils.join(sqlList, ", ")).append(" WHERE ").append(tableMeta.getKeyMeta().getName()).append("=?");
		Query query = new Query(buffer.toString());
		updatedList.stream().forEach(updated ->
		{
			query.append(updated.getColumnMeta(), updated.getNewValue());
		});
		return query.appendKey(key);
	}

	private QueryUtils()
	{
	}
}
