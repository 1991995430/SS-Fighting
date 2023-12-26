package com.ss.song.meta;

import com.ss.song.utils.RelationTable;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 表元数据
 *
 * @author wang.sheng
 *
 */
public final class TableMeta
{
	private static void getAllChildren(TableMeta tableMeta, List<TableMeta> list)
	{
		if (tableMeta.isLeaf())
		{
			return;
		}
		for (TableMeta child : tableMeta.children)
		{
			list.add(child);
			getAllChildren(child, list);// 递归调用
		}
	}

	private Class<?> tableClass;
	/**
	 * 主键属性
	 */
	private KeyMeta keyMeta;
	/**
	 * 表名称
	 */
	private String name;
	/**
	 * 常规字段
	 */
	private ColumnMeta[] columns;
	/**
	 * 对应上一级表是否单例
	 */
	private boolean singleton = false;
	private final List<TableMeta> children = new ArrayList<>();
	private TableMeta parent;
	/**
	 * 所在父表的字段
	 */
	private Field parentTableField;
	/**
	 * 对应父对象的本地字段
	 */
	private Field parentObjectField;
	/**
	 * 是否需要予以缓存
	 */
	private boolean cached = true;
	/**
	 * 与该表关联的多对多表(左,右关联均在范围内)
	 */
	private final Map<RelationTable, RelationMeta> relationTableMetaMap = new HashMap<>();

	public Class<?> getTableClass()
	{
		return tableClass;
	}

	public String foreignKeyName()
	{
		return this.name + "_" + this.keyMeta.getName();
	}

	public String foreignKeySqlType()
	{
		return this.keyMeta.sqlType();
	}

	/**
	 * 通过反射创建对象
	 *
	 * @return
	 */
	public Object newDataInstance()
	{
		try
		{
			return this.tableClass.newInstance();
		}
		catch (Exception e)
		{
			throw new RuntimeException("newInstance failed! class: " + tableClass, e);
		}
	}

	/**
	 * 存在关系映射
	 *
	 * @return
	 */
	public boolean existsRelation()
	{
		return !this.relationTableMetaMap.isEmpty();
	}

	/**
	 * 存在指定的关系映射
	 *
	 * @param relationTable
	 * @return
	 */
	public boolean existsRelationTable(RelationTable relationTable)
	{
		return this.relationTableMetaMap.containsKey(relationTable);
	}

	public TableMeta addRelationMeta(RelationMeta relationMeta)
	{
		this.relationTableMetaMap.put(relationMeta.getRelationTable(), relationMeta);
		return this;
	}

	public RelationMeta[] getRelationMetas()
	{
		return this.relationTableMetaMap.values().toArray(new RelationMeta[0]);
	}

	public void setTableClass(Class<?> tableClass)
	{
		this.tableClass = tableClass;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ColumnMeta getColumnByName(String name)
	{
		if (this.columns == null || this.columns.length < 1)
		{
			return null;
		}
		for (ColumnMeta column : this.columns)
		{
			if (StringUtils.equalsIgnoreCase(name, column.getName()))
			{
				return column;
			}
		}
		return null;
	}

	public ColumnMeta[] getColumns()
	{
		return columns;
	}

	public void setColumns(ColumnMeta[] columns)
	{
		this.columns = columns;
	}

	public boolean isSingleton()
	{
		return singleton;
	}

	public void setSingleton(boolean singleton)
	{
		this.singleton = singleton;
	}

	public void addChild(Field parentTableField, TableMeta tableMeta)
	{
		this.children.add(tableMeta);
		tableMeta.parentTableField = parentTableField;
		tableMeta.parent = this;
	}

	/**
	 * 获取直接父表元数据
	 *
	 * @return
	 */
	public TableMeta getParent()
	{
		return this.parent;
	}

	/**
	 * 获取全部父系表元数据集合
	 *
	 * @return
	 */
	public TableMeta[] getParents()
	{
		if (this.isRoot())
		{
			return new TableMeta[0];
		}
		List<TableMeta> list = new ArrayList<>();
		TableMeta parent = this.getParent();
		while (parent != null)
		{
			list.add(parent);
			parent = parent.getParent();
		}
		Collections.reverse(list);// 反向排序
		return list.toArray(new TableMeta[0]);
	}

	public Set<String> getForeignKeyNameSet()
	{
		Set<String> nameSet = new LinkedHashSet<>();
		for (TableMeta tableMeta : this.getParents())
		{
			nameSet.add(tableMeta.foreignKeyName());
		}
		return nameSet;
	}

	/**
	 * 获取下一级子表元数据集合
	 *
	 * @return
	 */
	public TableMeta[] getChildren()
	{
		return children.toArray(new TableMeta[0]);
	}

	/**
	 * 获取全部的子孙表元数据集合
	 *
	 * @param recursive
	 * @return
	 */
	public TableMeta[] getChildren(boolean recursive)
	{
		if (recursive)
		{
			List<TableMeta> list = new ArrayList<>();
			getAllChildren(this, list);
			return list.toArray(new TableMeta[0]);
		}
		else
		{
			return getChildren();
		}
	}

	/**
	 * 获取根表元数据
	 *
	 * @return
	 */
	public TableMeta getRoot()
	{
		TableMeta tableMeta = this;
		while (!tableMeta.isRoot())
		{
			tableMeta = tableMeta.getParent();
		}
		return tableMeta;
	}

	public boolean isLeaf()
	{
		return CollectionUtils.isEmpty(this.children);
	}

	public boolean isRoot()
	{
		return this.parent == null;
	}

	public KeyMeta getKeyMeta()
	{
		return keyMeta;
	}

	public void setKeyMeta(KeyMeta keyMeta)
	{
		this.keyMeta = keyMeta;
	}

	public Field getParentTableField()
	{
		return this.parentTableField;
	}

	public boolean isCached()
	{
		return cached;
	}

	public void setCached(boolean cached)
	{
		this.cached = cached;
	}

	public Field getParentObjectField()
	{
		return parentObjectField;
	}

	public void setParentObjectField(Field parentObjectField)
	{
		this.parentObjectField = parentObjectField;
	}

}
