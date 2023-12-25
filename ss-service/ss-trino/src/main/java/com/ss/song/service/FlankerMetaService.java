package com.ss.song.service;

import com.ss.song.annotation.*;
import com.ss.song.enums.FlankerErrorType;
import com.ss.song.enums.None;
import com.ss.song.enums.SqlType;
import com.ss.song.meta.ColumnMeta;
import com.ss.song.meta.KeyMeta;
import com.ss.song.meta.RelationMeta;
import com.ss.song.meta.TableMeta;
import com.ss.song.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 表元数据服务
 *
 * @author wang.sheng
 *
 */
@Service
public class FlankerMetaService
{
	private final Map<Class<?>, TableMeta> tableMetaMap = new HashMap<>();
	private final Set<RelationTable> relationTableSet = new HashSet<>();

	public Map<Class<?>, TableMeta> getTableMetaMap () {
		return this.tableMetaMap;
	}

	@Resource
	private JdbcTemplate jdbcTemplate;

	Log log = LogFactory.getLog(getClass());

	/**
	 * 扫描指定的包路径,加载全部的元数据
	 *
	 * @param packages
	 */
	public void scan(String... packages)
	{
		// 根表结构解析和同步
		tableMetaMap.clear();
		relationTableSet.clear();
		ClassScanner.scan(packages).filter(clazz -> clazz.isAnnotationPresent(FlankerTable.class)).map(clazz -> loadTableMeta(clazz)).forEach(tableMeta -> tableMetaMap.put(tableMeta.getTableClass(), tableMeta));
		TableMeta[] tableMetas = tableMetaMap.values().toArray(new TableMeta[0]);
		// 遍历并设置父子关系
		for (TableMeta tableMeta : tableMetas)
		{
			Class<?> tableClass = tableMeta.getTableClass();
			// 检查1对多子对象
			Field[] fields = ReflectUtils.getAllFields(tableClass, FlankerChildren.class);
			for (Field field : fields)
			{
				FlankerChildren children = field.getAnnotation(FlankerChildren.class);
				Class<?> childTableClass = children.singleton() ? field.getType() : children.childClass();
				if (childTableClass == Object.class)
				{
					// 子表类型未设置
					//throw new StandardException(FlankerErrorType.Metadata.INVALID_TABLE_CLASS, childTableClass.getName());
				}
				TableMeta childTableMeta = tableMetaMap.get(childTableClass);
				if (childTableMeta == null)
				{
					// 子表不存在
					//throw new StandardException(FlankerErrorType.Metadata.TABLE_NOT_EXISTS, childTableClass.getName());
				}
				if (childTableMeta.getParent() != null)
				{
					// 已设置父对象
					//throw new StandardException(FlankerErrorType.Metadata.PARENT_TABLE_EXISTS, childTableClass.getName());
				}
				childTableMeta.setSingleton(children.singleton());
				tableMeta.addChild(field, childTableMeta);
			}
		}
		// 初始化关系
		for (TableMeta leftTableMeta : tableMetas)
		{
			// 检查多对多子对象
			Field[] relationFields = ReflectUtils.getAllFields(leftTableMeta.getTableClass(), FlankerRelation.class);
			for (Field relationField : relationFields)
			{
				FlankerRelation flankerRelation = relationField.getAnnotation(FlankerRelation.class);
				if (!Set.class.isAssignableFrom(relationField.getType()))
				{
					// 关系属性必须是Set<String>集合类型
					//throw new StandardException(FlankerErrorType.Metadata.INVALID_RELATION_FIELD_DEFINE, relationField.getType().getName());
				}
				Class<?> otherTableClass = flankerRelation.target();
				TableMeta rightTableMeta = this.getRootTableMeta(otherTableClass);// 找到关联表对应的元数据,对端必须是根表
				switch (flankerRelation.relationType())
				{
				case LEFT:
					if (!rightTableMeta.isRoot())
					{
						// 右表必须是根表
						//throw new StandardException(FlankerErrorType.Metadata.TABLE_NOT_ROOT, otherTableClass.getName());
					}
					break;
				case RIGHT:
					if (!leftTableMeta.isRoot())
					{
						// 右表必须是根表
						//throw new StandardException(FlankerErrorType.Metadata.TABLE_NOT_ROOT, leftTableMeta.getTableClass().getName());
					}
					break;
				}
				RelationMeta relationMeta = new RelationMeta(relationField, flankerRelation.relationType(), leftTableMeta.getTableClass(), rightTableMeta.getTableClass());
				leftTableMeta.addRelationMeta(relationMeta);// 添加多对多关系映射
				this.relationTableSet.add(relationMeta.getRelationTable());
			}
		}
		// 同步数据表和关系表的表结构
		this.syncToDB(tableMetas);
	}
	/**
	 * 同步到DB的表结构中
	 *
	 * @param tableMetas
	 */
	private void syncToDB(TableMeta[] tableMetas)
	{
		final Set<String> tableNameSet = jdbcTemplate.queryForList("show tables", String.class).stream().map(name -> StringUtils.lowerCase(name)).collect(Collectors.toSet());
		List<Query> queryList = new ArrayList<>();
		// 数据表结构
		for (TableMeta tableMeta : tableMetas)
		{
			String tableName = tableMeta.getName();
			if (!tableNameSet.contains(StringUtils.lowerCase(tableName)))
			{
				// 表不存在,准备创建表
				queryList.add(QueryUtils.createSql(tableMeta));
			}
			else
			{
				// 进行字段检查,自动添加不存在的字段
				Set<String> fieldNameSet = jdbcTemplate.query("desc " + tableMeta.getName(), new RowMapper<String>()
				{
					@Override
					public String mapRow(ResultSet rs, int rowNum) throws SQLException
					{
						return rs.getString(1);
					}
				}).stream().map(name -> StringUtils.lowerCase(name)).collect(Collectors.toSet());
				// 检查外键
				for (TableMeta parent : tableMeta.getParents())
				{
					String foreignKeyName = parent.foreignKeyName();
					if (!fieldNameSet.contains(StringUtils.lowerCase(foreignKeyName)))
					{
						// 外键不存在
						queryList.add(new Query("ALTER TABLE " + tableName + " ADD COLUMN " + foreignKeyName + " " + parent.foreignKeySqlType() + " NOT NULL DEFAULT ''"));
					}
				}
				// 检查常规字段
				for (ColumnMeta column : tableMeta.getColumns())
				{
					String columnName = StringUtils.lowerCase(column.getName());
					if (!fieldNameSet.contains(columnName))
					{
						// 字段不存在
						queryList.add(new Query("ALTER TABLE " + tableName + " ADD COLUMN " + column.sqlString()));
					}
				}
			}
			// 检查关系映射
			if (tableMeta.existsRelation())
			{
				for (RelationMeta relationMeta : tableMeta.getRelationMetas())
				{
					RelationTable relationTable = relationMeta.getRelationTable();
					if (!tableNameSet.contains(StringUtils.lowerCase(relationTable.getName())))
					{
						// 需要创建关系表
						queryList.add(QueryUtils.createSql(relationTable));
						tableNameSet.add(StringUtils.lowerCase(relationTable.getName()));
					}
				}
			}
		}
		if (!CollectionUtils.isEmpty(queryList))
		{
			for (Query query : queryList)
			{
				log.info("sync sql: " + query.getSql());
				jdbcTemplate.update(query.getSql(), query.getParams());
			}
			queryList.clear();
		}
	}

	public RelationTable[] getRelationTables(Class<?> tableClass)
	{
		List<RelationTable> list = new ArrayList<>();
		for (RelationTable relationTable : this.relationTableSet)
		{
			if (relationTable.getLeft() == tableClass || relationTable.getRight() == tableClass)
			{
				list.add(relationTable);
			}
		}
		return list.toArray(new RelationTable[0]);
	}

	public boolean isRootTableMeta(Class<?> tableClass)
	{
		TableMeta tableMeta = this.tableMetaMap.get(tableClass);
		return tableMeta != null && tableMeta.isRoot();
	}

	/**
	 * 获取指定表的元数据
	 *
	 * @param tableClass
	 * @return
	 */
	public TableMeta getTableMeta(Class<?> tableClass)
	{
		if (this.tableMetaMap.containsKey(tableClass))
		{
			return this.tableMetaMap.get(tableClass);
		}
		else
		{
			// 元数据不存在
			throw new RuntimeException(FlankerErrorType.Metadata.TABLE_NOT_EXISTS.message());
		}
	}

	/**
	 * 根据类获取表的元数据.如不存在则抛出异常
	 *
	 * @param tableClass
	 * @return
	 */
	public TableMeta getRootTableMeta(Class<?> tableClass)
	{
		if (this.tableMetaMap.containsKey(tableClass))
		{
			TableMeta tableMeta = this.tableMetaMap.get(tableClass);
			if (!tableMeta.isRoot())
			{
				// 非根节点
				throw new RuntimeException(FlankerErrorType.Metadata.TABLE_NOT_ROOT.message());
			}
			return tableMeta;
		}
		else
		{
			// 元数据不存在
			throw new RuntimeException(FlankerErrorType.Metadata.TABLE_NOT_EXISTS.message());
		}
	}

	/**
	 * 获取全部根节点类型的元数据集合
	 *
	 * @return
	 */
	public TableMeta[] getRootTableMetas()
	{
		return this.tableMetaMap.values().stream().filter(tableMeta -> tableMeta.isRoot()).collect(Collectors.toList()).toArray(new TableMeta[0]);
	}

	private TableMeta loadTableMeta(Class<?> tableClass)
	{
		FlankerTable table = tableClass.getAnnotation(FlankerTable.class);
		TableMeta tableMeta = new TableMeta();
		tableMeta.setName(StringUtils.isEmpty(table.name()) ? tableClass.getSimpleName() : table.name());
		tableMeta.setTableClass(tableClass);
		tableMeta.setCached(table.cached());
		Field[] fields = ReflectUtils.getAllFields(tableClass);
		List<ColumnMeta> columnList = new ArrayList<>();
		for (Field field : fields)
		{
			if (field.isAnnotationPresent(FlankerKey.class))
			{
				// 主键字段
				FlankerKey flankerKey = field.getAnnotation(FlankerKey.class);
				KeyMeta keyMeta = new KeyMeta();
				keyMeta.setField(field);
				keyMeta.setGeneratorType(flankerKey.generatorType());
				keyMeta.setName(StringUtils.isEmpty(flankerKey.name()) ? field.getName() : flankerKey.name());
				keyMeta.setLength(flankerKey.length());
				tableMeta.setKeyMeta(keyMeta);
			}
			else if (field.isAnnotationPresent(FlankerColumn.class))
			{
				// 常规字段
				FlankerColumn column = field.getAnnotation(FlankerColumn.class);
				ColumnMeta columnMeta = new ColumnMeta();
				columnMeta.setName(StringUtils.isEmpty(column.name()) ? field.getName() : column.name());
				columnMeta.setReadonly(!column.updatable());
				if (!column.updatable())
				{
					// 只读字段必填
					columnMeta.setNullable(false);
				}
				else
				{
					columnMeta.setNullable(column.nullable());
				}
				Class<?> fieldType = field.getType();
				if (EnumSet.class.isAssignableFrom(fieldType))
				{
					// 枚举集合类型
					Class<? extends Enum<?>> enumType = column.enumType();
					if (enumType == None.class)
					{
						//throw new StandardRuntimeException(FlankerErrorType.Metadata.UNKNOWN_ENUM_TYPE, field.getName());
					}
					columnMeta.setEnumType(enumType);
				}
				SqlType sqlType = column.sqlType() != SqlType.VARCHAR ? column.sqlType() : DataUtils.toSqlType(fieldType);// 未指定类型时,根据属性类型推断字段类型
				columnMeta.setSqlType(sqlType);
				switch (sqlType)
				{
				case VARCHAR:
					columnMeta.setLength(column.length());
					break;
				case DECIMAL:
					columnMeta.setPrecision(column.precision());
					columnMeta.setScale(column.scale());
					break;
				default:
					break;
				}
				columnMeta.setField(field);
				columnList.add(columnMeta);
			}
			else if (field.isAnnotationPresent(FlankerParent.class))
			{
				// 标识为上一级对象的属性
				tableMeta.setParentObjectField(field);
			}
		}
		if (tableMeta.getKeyMeta() == null)
		{
			// 无主键字段
			//throw new StandardRuntimeException(FlankerErrorType.Metadata.KEY_NOT_EXISTS, tableMeta.getTableClass().getName());
		}
		tableMeta.setColumns(columnList.toArray(new ColumnMeta[0]));
		return tableMeta;
	}
}
