package com.ss.song.utils;


import com.ss.song.meta.TableMeta;

public final class TableData
{
	private TableMeta tableMeta;
	private Object data;

	public TableData(TableMeta tableMeta, Object data)
	{
		if (tableMeta.getTableClass() != data.getClass())
		{
			throw new IllegalArgumentException("Invalid tableMeta and data: " + tableMeta.getName() + ", data: " + data);
		}
		this.tableMeta = tableMeta;
		this.data = data;
	}

	public Class<?> getTableClass()
	{
		return this.tableMeta.getTableClass();
	}

	public TableMeta getTableMeta()
	{
		return tableMeta;
	}

	public Object getData()
	{
		return data;
	}

}
