package com.ss.song.model;

import com.ss.song.annotation.FlankerColumn;
import com.ss.song.annotation.FlankerKey;
import com.ss.song.annotation.FlankerTable;

@FlankerTable
public class JdbcResource
{
	private static final long serialVersionUID = 1L;

	@FlankerKey
	private String id;
	private String name;
	private String remark;
	@FlankerColumn(length = 100)
	private String driverClass;
	@FlankerColumn(length = 200)
	private String jdbcUrl;
	@FlankerColumn(length = 50)
	private String password;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}


	public String getDriverClass()
	{
		return driverClass;
	}

	public void setDriverClass(String driverClass)
	{
		this.driverClass = driverClass;
	}

	public String getJdbcUrl()
	{
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl)
	{
		this.jdbcUrl = jdbcUrl;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

}
