package com.ss.song.model;

import org.apache.commons.lang.StringUtils;

public final class Indexed
{
	private Class<?> tableClass;
	private String key;

	public Indexed(Class<?> tableClass, String key)
	{
		this.tableClass = tableClass;
		this.key = key;
	}

	public Class<?> getTableClass()
	{
		return tableClass;
	}

	public String getKey()
	{
		return key;
	}

	@Override
	public int hashCode()
	{
		return this.tableClass.hashCode() + this.key.hashCode() * 7;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other == null || !(other instanceof Indexed))
		{
			return false;
		}
		Indexed indexed = (Indexed) other;
		return indexed.tableClass == this.tableClass && StringUtils.equals(this.key, indexed.key);
	}

	@Override
	public String toString()
	{
		return new StringBuffer().append(tableClass.getSimpleName()).append(":").append(key).toString();
	}
}
