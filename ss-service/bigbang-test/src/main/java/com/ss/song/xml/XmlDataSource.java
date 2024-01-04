package com.ss.song.xml;

import com.ss.song.annotation.XmlAttribute;

public class XmlDataSource
{
	@XmlAttribute
	private String driverClass;
	@XmlAttribute
	private String jdbcUrl;
	@XmlAttribute
	private String user;
	@XmlAttribute
	private String password;
	@XmlAttribute
	private int maxPoolSize = 50;

	public String getDriverClass()
	{
		return driverClass;
	}

	public String getJdbcUrl()
	{
		return jdbcUrl;
	}

	public String getUser()
	{
		return user;
	}

	public String getPassword()
	{
		return password;
	}

	public int getMaxPoolSize()
	{
		return maxPoolSize;
	}

}
