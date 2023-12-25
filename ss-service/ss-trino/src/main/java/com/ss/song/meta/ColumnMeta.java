package com.ss.song.meta;


import com.ss.song.enums.SqlType;

import java.lang.reflect.Field;

public class ColumnMeta
{
	private String name;
	private SqlType sqlType;
	private Field field;
	private boolean nullable;
	private boolean readonly;
	private int length = 0;
	private int precision = 0;
	private int scale = 0;
	private String dictionary;
	@SuppressWarnings("rawtypes")
	private Class<? extends Enum> enumType;

	public String sqlString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(name).append(" ").append(sqlType.name());
		switch (sqlType)
		{
		case VARCHAR:
			buffer.append("(").append(this.length).append(")");
			break;
		case DECIMAL:
			buffer.append("(").append(this.precision).append(",").append(this.scale).append(")");
			break;
		default:
			break;
		}
		if (!this.nullable)
		{
			buffer.append(" NOT NULL");
		}
		return buffer.toString();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public SqlType getSqlType()
	{
		return sqlType;
	}

	public void setSqlType(SqlType sqlType)
	{
		this.sqlType = sqlType;
	}

	public Field getField()
	{
		return field;
	}

	public void setField(Field field)
	{
		this.field = field;
	}

	public boolean isNullable()
	{
		return nullable;
	}

	public void setNullable(boolean nullable)
	{
		this.nullable = nullable;
	}

	public boolean isReadonly()
	{
		return readonly;
	}

	public void setReadonly(boolean readonly)
	{
		this.readonly = readonly;
	}

	public String getDictionary()
	{
		return dictionary;
	}

	public void setDictionary(String dictionary)
	{
		this.dictionary = dictionary;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public int getPrecision()
	{
		return precision;
	}

	public void setPrecision(int precision)
	{
		this.precision = precision;
	}

	public int getScale()
	{
		return scale;
	}

	public void setScale(int scale)
	{
		this.scale = scale;
	}

	@SuppressWarnings("rawtypes")
	public Class<? extends Enum> getEnumType()
	{
		return enumType;
	}

	@SuppressWarnings("rawtypes")
	public void setEnumType(Class<? extends Enum> enumType)
	{
		this.enumType = enumType;
	}

}
