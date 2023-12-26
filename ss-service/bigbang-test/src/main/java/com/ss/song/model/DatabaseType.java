package com.ss.song.model;


import com.ss.song.enums.Displayable;

public enum DatabaseType implements Displayable
{
	MYSQL("MySQL"), MSSQL("Microsoft SQL Server"), ORACLE("Oracle"), PG("PostgreSQL");

	private String display;

	private DatabaseType(String display)
	{
		this.display = display;
	}

	@Override
	public String toString()
	{
		return super.name() + "(" + this.display + ")";
	}

	@Override
	public String display()
	{
		return this.display;
	}
}
