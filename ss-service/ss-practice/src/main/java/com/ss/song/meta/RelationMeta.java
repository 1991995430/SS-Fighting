package com.ss.song.meta;


import com.ss.song.enums.RelationType;
import com.ss.song.utils.RelationTable;

import java.lang.reflect.Field;

/**
 * 关系元数据
 *
 * @author wang.sheng
 *
 */
public final class RelationMeta
{
	/**
	 * 所在对象的属性
	 */
	private Field field;
	/**
	 * 关系表
	 */
	private RelationTable relationTable;

	public RelationMeta(Field field, RelationType relationType, Class<?> leftTableClass, Class<?> rightTableClass)
	{
		this.field = field;
		switch (relationType)
		{
		case LEFT:
			this.relationTable = new RelationTable(leftTableClass, rightTableClass);
			break;
		case RIGHT:
			this.relationTable = new RelationTable(rightTableClass, leftTableClass);
			break;
		}
	}

	public RelationTable getRelationTable()
	{
		return relationTable;
	}

	public Field getField()
	{
		return field;
	}

}
