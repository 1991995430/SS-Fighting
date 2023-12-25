package com.ss.song.utils;


import com.ss.song.meta.ColumnMeta;

public class Updated
{
	private ColumnMeta columnMeta;
	private Object oldValue;
	private Object newValue;

	public Updated(ColumnMeta columnMeta, Object oldValue, Object newValue)
	{
		this.columnMeta = columnMeta;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public ColumnMeta getColumnMeta()
	{
		return columnMeta;
	}

	public Object getOldValue()
	{
		return oldValue;
	}

	public Object getNewValue()
	{
		return newValue;
	}

}
