package com.ss.song.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("JDBC资源")
public class JdbcResource
{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键ID")
	private String id;
	@ApiModelProperty("名称")
	private String name;
	@ApiModelProperty("备注")
	private String remark;
	@ApiModelProperty("数据库类型枚举:/api/system/enum/DatabaseType")
	private DatabaseType databaseType;
	@ApiModelProperty("驱动包名")
	private String driverClass;
	@ApiModelProperty("JDBC连接字符串")
	private String jdbcUrl;
	@ApiModelProperty("用户名")
	private String username;
	@ApiModelProperty("密码")
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

	public DatabaseType getDatabaseType()
	{
		return databaseType;
	}

	public void setDatabaseType(DatabaseType databaseType)
	{
		this.databaseType = databaseType;
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

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
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
