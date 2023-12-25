package com.ss.song.service;

import com.ss.song.dao.FlankerJdbcTemplate;
import com.ss.song.meta.RelationMeta;
import com.ss.song.meta.TableMeta;
import com.ss.song.model.Path;
import com.ss.song.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 多对多关系服务
 *
 * @author wang.sheng
 *
 */
@Service
public class RelationService
{
	@Resource
	private FlankerJdbcTemplate flankerJdbcTemplate;
	@Resource
	private FlankerMetaService flankerMetaService;

	Log log = LogFactory.getLog(getClass());

	/**
	 * 加载对象中的关系
	 *
	 * @param tableMeta
	 * @param data
	 * @param recurse
	 */
	@SuppressWarnings("unchecked")
	public void loadRelation(TableMeta tableMeta, Object data, boolean recurse)
	{
		if (recurse)
		{
			// 递归遍历全部子对象,并进行加载
			DataUtils.dataStream(new TableData(tableMeta, data)).filter(tableData -> tableData.getTableMeta().existsRelation()).forEach(tableData ->
			{
				// 存在关系映射的对象
				loadRelation(tableData.getTableMeta(), tableData.getData(), false);// 递归调用
			});
		}
		else if (tableMeta.existsRelation())
		{
			String key = (String) ReflectUtils.getFieldValue(data, tableMeta.getKeyMeta().getField());
			for (RelationMeta relationMeta : tableMeta.getRelationMetas())
			{
				Set<String> relationFieldValue = (Set<String>) ReflectUtils.getFieldValue(data, relationMeta.getField());
				if (relationFieldValue == null)
				{
					// 初始化该字段
					relationFieldValue = new HashSet<>();
					ReflectUtils.setFieldValue(data, relationMeta.getField(), relationFieldValue);
				}
				// 从关系表中查询关联KEY集合
				RelationTable relationTable = relationMeta.getRelationTable();
				Set<String> relationKeySet = this.flankerJdbcTemplate.queryRelationIdSet(relationTable, tableMeta.getTableClass(), key).stream().collect(Collectors.toSet());
				relationFieldValue.clear();
				relationFieldValue.addAll(relationKeySet);
			}
		}
	}

	/**
	 * 添加对象中的关系
	 *
	 * @param tableMeta
	 * @param data
	 * @param recurse
	 * @param insertQueryList
	 */
	@SuppressWarnings("unchecked")
	public void insertRelation(TableMeta tableMeta, Object data, boolean recurse, List<Query> insertQueryList)
	{
		if (recurse)
		{
			// 递归遍历全部子对象,并进行加载
			DataUtils.dataStream(new TableData(tableMeta, data)).filter(tableData -> tableData.getTableMeta().existsRelation()).forEach(tableData ->
			{
				// 存在关系映射的对象
				insertRelation(tableData.getTableMeta(), tableData.getData(), false, insertQueryList);// 递归调用
			});
		}
		else if (tableMeta.existsRelation())
		{
			String key = (String) ReflectUtils.getFieldValue(data, tableMeta.getKeyMeta().getField());
			for (RelationMeta relationMeta : tableMeta.getRelationMetas())
			{
				Set<String> otherKeySet = (Set<String>) ReflectUtils.getFieldValue(data, relationMeta.getField());
				if (CollectionUtils.isEmpty(otherKeySet))
				{
					// 为空时直接忽略
					continue;
				}
				// 从关系表中查询关联KEY集合
				RelationTable relationTable = relationMeta.getRelationTable();
				insertQueryList.addAll(QueryUtils.insertSql(relationTable, tableMeta.getTableClass(), key, otherKeySet));
				// 查看对端对象是否需要重新加载
				Class<?> otherTableClass = relationTable.toOtherTableClass(tableMeta.getTableClass());
				TableMeta otherTableMeta = flankerMetaService.getTableMeta(otherTableClass);
				if (otherTableMeta.existsRelationTable(relationTable))
				{
					// 对端同样存在指定的映射.这种情况比较少见
					for (String otherKey : otherKeySet)
					{
						Path otherPath = flankerJdbcTemplate.getPath(otherTableMeta, otherKey);
						//FlankerSession.current().append(otherPath);// 添加到会话中,待刷新
					}
				}
			}
		}
	}

	/**
	 * 删除对象中的关系
	 *
	 * @param tableMeta
	 * @param data
	 * @param recurse
	 * @param insertQueryList
	 */
	@SuppressWarnings("unchecked")
	public void deleteRelation(TableMeta tableMeta, Object data, boolean recurse, List<Query> deleteQueryList)
	{
		if (recurse)
		{
			// 递归遍历全部子对象,并进行加载
			DataUtils.dataStream(new TableData(tableMeta, data)).filter(tableData -> tableData.getTableMeta().existsRelation()).forEach(tableData ->
			{
				// 存在关系映射的对象
				deleteRelation(tableData.getTableMeta(), tableData.getData(), false, deleteQueryList);// 递归调用
			});
		}
		else if (tableMeta.existsRelation())
		{
			String key = (String) ReflectUtils.getFieldValue(data, tableMeta.getKeyMeta().getField());
			for (RelationMeta relationMeta : tableMeta.getRelationMetas())
			{
				Set<String> otherKeySet = (Set<String>) ReflectUtils.getFieldValue(data, relationMeta.getField());
				if (CollectionUtils.isEmpty(otherKeySet))
				{
					// 为空时直接忽略
					continue;
				}
				// 从关系表中查询关联KEY集合
				RelationTable relationTable = relationMeta.getRelationTable();
				deleteQueryList.add(QueryUtils.deleteSql(relationTable, tableMeta.getTableClass(), key));
				// 查看对端对象是否需要重新加载
				Class<?> otherTableClass = relationTable.toOtherTableClass(tableMeta.getTableClass());
				TableMeta otherTableMeta = flankerMetaService.getTableMeta(otherTableClass);
				if (otherTableMeta.existsRelationTable(relationTable))
				{
					// 对端同样存在指定的映射.这种情况比较少见
					for (String otherKey : otherKeySet)
					{
						Path otherPath = flankerJdbcTemplate.getPath(otherTableMeta, otherKey);
						//FlankerSession.current().append(otherPath);// 添加到会话中,待刷新
					}
				}
			}
		}
	}
}
