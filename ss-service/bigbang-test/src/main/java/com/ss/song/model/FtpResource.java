package com.ss.song.model;

import com.ss.song.annotation.FlankerColumn;
import com.ss.song.annotation.FlankerKey;
import com.ss.song.annotation.FlankerTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@FlankerTable
@ApiModel("文件资源")
public class FtpResource
{
	private static final long serialVersionUID = 1L;

	@FlankerKey
	@ApiModelProperty("主键ID")
	private String id;
	@FlankerColumn(length = 50, nullable = false)
	@ApiModelProperty("名称")
	private String name;
	@FlankerColumn(length = 200)
	@ApiModelProperty("备注")
	private String remark;
	@FlankerColumn(length = 50, nullable = false)
	@ApiModelProperty("服务器地址")
	private String host;
	@FlankerColumn(length = 200)
	@ApiModelProperty("相对路径")
	private String path;
	@FlankerColumn
	@ApiModelProperty("端口")
	private int port = 21;
	@FlankerColumn(length = 50)
	@ApiModelProperty("用户名")
	private String username;
	@FlankerColumn(length = 50)
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

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
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

	public void setPath(String path)
	{
		this.path = path;
	}

}
