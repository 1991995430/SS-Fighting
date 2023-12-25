package com.ss.song.dao;

import com.ss.song.meta.TableMeta;
import com.ss.song.model.IndexedData;
import com.ss.song.model.Path;
import com.ss.song.service.FlankerMetaService;
import com.ss.song.utils.Query;
import com.ss.song.utils.QueryUtils;
import com.ss.song.utils.RelationTable;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class FlankerJdbcTemplate
{
	private static final Map<Class<?>, TableRowMapper> tableRowMapperMap = new HashMap<>();

	public static TableRowMapper getTableRowMapper(TableMeta tableMeta)
	{
		Class<?> tableClass = tableMeta.getTableClass();
		if (tableRowMapperMap.containsKey(tableClass))
		{
			return tableRowMapperMap.get(tableClass);
		}
		TableRowMapper tableRowMapper = new TableRowMapper(tableMeta);
		tableRowMapperMap.put(tableClass, tableRowMapper);
		return tableRowMapper;
	}

	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private FlankerMetaService flankerMetaService;

	Log log = LogFactory.getLog(getClass());

	/**
	 * 查询单条记录
	 *
	 * @param tableMeta
	 * @param sql
	 * @param params
	 * @return
	 */
	public IndexedData queryForObject(TableMeta tableMeta, String sql, Object... params)
	{
		log.info("queryForObject sql: " + sql + ", params: [" + StringUtils.join(params, "; ") + "]");
		return jdbcTemplate.queryForObject(sql, getTableRowMapper(tableMeta), params);
	}

	public IndexedData queryForFirstObject(TableMeta tableMeta, String sql, Object... params)
	{
		log.info("queryForFirstObject sql: " + sql + ", params: [" + StringUtils.join(params, "; ") + "]");
		List<IndexedData> list = this.queryForList(tableMeta, sql, params);
		if (list.isEmpty())
		{
			return null;
		}
		return list.get(0);
	}

	/**
	 * 根据表名和KEY查询记录<br>
	 * 该路径为完整路径<br>
	 *
	 * @param tableMeta
	 * @param key
	 * @return
	 */
	public Path getPath(final TableMeta tableMeta, String key)
	{
		if (StringUtils.isEmpty(key))
		{
			//throw new StandardRuntimeException(FlankerErrorType.Query.KEY_CANNOT_EMPTY);
		}
		if (tableMeta.isRoot())
		{
			// 根表
			return new Path(tableMeta.getTableClass(), key);
		}
		else
		{
			// 非根表
			String sql = "SELECT " + StringUtils.join(tableMeta.getForeignKeyNameSet(), ",") + " FROM " + tableMeta.getName() + " WHERE " + tableMeta.getKeyMeta().getName() + "=?";
			return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
			{
				Path path = new Path();
				for (TableMeta parentTableMeta : tableMeta.getParents())
				{
					String foreignKey = rs.getString(parentTableMeta.foreignKeyName());
					path.append(parentTableMeta.getTableClass(), foreignKey);
				}
				path.append(tableMeta.getTableClass(), key);
				return path;
			}, key);
		}
	}

	/**
	 * 查询多条记录
	 *
	 * @param tableMeta
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<IndexedData> queryForList(TableMeta tableMeta, String sql, Object... params)
	{
		log.info("queryForList sql: " + sql + ", params: [" + StringUtils.join(params, "; ") + "]");
		return jdbcTemplate.query(sql, getTableRowMapper(tableMeta), params);
	}

	/**
	 * 查询索引对象集合
	 *
	 * @param tableClass
	 * @param sql
	 * @param params
	 * @return
	 */
	public <T> List<T> queryForList(Class<T> tableClass, String sql, Object... params)
	{
		TableRowMapper tableRowMapper = tableRowMapperMap.get(tableClass);
		if (tableRowMapper == null)
		{
			//hrow new StandardRuntimeException(FlankerErrorType.Metadata.TABLE_NOT_EXISTS, tableClass.getSimpleName());
		}
		return jdbcTemplate.query(sql, tableRowMapper, params).stream().map(data -> tableClass.cast(data)).collect(Collectors.toList());
	}

	/**
	 * 查询指定索引对象
	 *
	 * @param tableClass
	 * @param sql
	 * @param params
	 * @return
	 */
	public <T> T queryForObject(Class<T> tableClass, String sql, Object... params)
	{
		TableRowMapper tableRowMapper = tableRowMapperMap.get(tableClass);
		if (tableRowMapper == null)
		{
			//throw new StandardRuntimeException(FlankerErrorType.Metadata.TABLE_NOT_EXISTS, tableClass.getSimpleName());
		}
		return tableClass.cast(jdbcTemplate.queryForObject(sql, tableRowMapper, params));
	}

	public <T> T queryForFirstObject(Class<T> tableClass, String sql, Object... params)
	{
		List<T> list = this.queryForList(tableClass, sql, params);
		if (list.isEmpty())
		{
			return null;
		}
		return list.get(0);
	}

	public void insert(IndexedData indexedData)
	{
		Class<?> tableClass = indexedData.getRaw().getClass();
		TableMeta tableMeta = this.flankerMetaService.getTableMeta(tableClass);
		Query insertQuery = QueryUtils.insertSql(tableMeta, indexedData);
		this.executeUpdate(insertQuery);
	}

	public void insertRoot(Object rawData)
	{
		Class<?> tableClass = rawData.getClass();
		TableMeta tableMeta = this.flankerMetaService.getRootTableMeta(tableClass);
		Query insertQuery = QueryUtils.insertSql(tableMeta, new IndexedData(rawData));
		this.executeUpdate(insertQuery);
	}

	public void update(Object rawData)
	{
		Class<?> tableClass = rawData.getClass();
		TableMeta tableMeta = this.flankerMetaService.getTableMeta(tableClass);
		Query updateQuery = QueryUtils.updateSql(tableMeta, rawData);
		this.executeUpdate(updateQuery);
	}

	public void delete(Object rawData)
	{
		Class<?> tableClass = rawData.getClass();
		TableMeta tableMeta = this.flankerMetaService.getTableMeta(tableClass);
		Query deleteQuery = QueryUtils.deleteSql(tableMeta, rawData);
		this.executeUpdate(deleteQuery);
	}

	/**
	 * 查询分页索引对象
	 *
	 * @param tableClass
	 * @param sql
	 * @param start
	 * @param limit
	 * @param params
	 * @return
	 */
	/*public <T> PagingResult<T> queryForPagingResult(Class<T> tableClass, String sql, int start, int limit, Object... params)
	{
		TableRowMapper tableRowMapper = tableRowMapperMap.get(tableClass);
		if (tableRowMapper == null)
		{
			throw new StandardRuntimeException(FlankerErrorType.Metadata.TABLE_NOT_EXISTS, tableClass.getSimpleName());
		}
		String countSql = "SELECT COUNT(1) FROM (" + sql + ")";
		log.info("queryForPagingResult count sql: " + countSql);
		long totalCount = jdbcTemplate.queryForObject(countSql, Long.class, params);
		if (totalCount < 1L)
		{
			return PagingResult.empty();
		}
		String selectSql = "SELECT * FROM (" + sql + ") LIMIT " + start + "," + limit;
		log.info("queryForPagingResult select sql: " + selectSql);
		List<T> list = this.queryForList(tableClass, selectSql, params);
		return new PagingResult<>(totalCount, list);
	}*/

	/**
	 * 执行insert/update/delete语句
	 *
	 * @param query
	 */
	public void executeUpdate(Query query)
	{
		log.info("executeUpdate " + query);
		jdbcTemplate.update(query.getSql(), query.getParams());
	}

	/**
	 * 连续执行insert/update/delete语句
	 *
	 * @param queryList
	 */
	public void executeUpdate(List<Query> queryList)
	{
		if (CollectionUtils.isEmpty(queryList))
		{
			return;
		}
		for (Query query : queryList)
		{
			this.executeUpdate(query);
		}
	}

	/**
	 * 连续执行insert/update/delete语句
	 *
	 * @param querys
	 */
	public void executeUpdate(Query... querys)
	{
		if (querys == null || querys.length < 1)
		{
			return;
		}
		for (Query query : querys)
		{
			this.executeUpdate(query);
		}
	}

	/**
	 * 获取指定记录的关联表KEY集合
	 *
	 * @param relationTable
	 * @param tableClass
	 * @param key
	 * @return
	 */
	public Set<String> queryRelationIdSet(RelationTable relationTable, Class<?> tableClass, String key)
	{
		String sql = "SELECT " + relationTable.toOtherColumnName(tableClass) + " FROM " + relationTable.getName() + " WHERE " + relationTable.toColumnName(tableClass) + "=?";
		log.info("queryRelationIdSet sql: " + sql + ", param: " + key);
		return jdbcTemplate.queryForList(sql, String.class, key).stream().collect(Collectors.toSet());
	}
}
