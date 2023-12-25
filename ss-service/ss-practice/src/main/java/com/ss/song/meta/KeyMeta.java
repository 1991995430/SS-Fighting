package com.ss.song.meta;


import com.ss.song.enums.GeneratorType;

import java.lang.reflect.Field;

public class KeyMeta
{
	private String name;
	private Field field;
	private GeneratorType generatorType;
	private int length;

	public String sqlString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(name).append(" ").append(sqlType()).append(" PRIMARY KEY");
		return buffer.toString();
	}

	public String sqlType()
	{
		return "VARCHAR(" + this.length + ")";
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Field getField()
	{
		return field;
	}

	public void setField(Field field)
	{
		this.field = field;
	}

	public GeneratorType getGeneratorType()
	{
		return generatorType;
	}

	public void setGeneratorType(GeneratorType generatorType)
	{
		this.generatorType = generatorType;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

}
