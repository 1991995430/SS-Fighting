package com.ss.song.model;

import com.ss.song.annotation.FlankerColumn;
import com.ss.song.annotation.FlankerKey;
import com.ss.song.annotation.FlankerTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 离线资源
 *
 * @author wang.sheng
 *
 */
@FlankerTable
@ApiModel("离线资源配置")
public class OfflineResource implements Serializable
{
	private static final long serialVersionUID = 1L;

	@FlankerKey
	@ApiModelProperty("资源唯一标识ID")
	private String id;
	@FlankerColumn(length = 100, nullable = false)
	@ApiModelProperty("文件名正则")
	private String fileName;
	@FlankerColumn(length = 50, nullable = false)
	@ApiModelProperty("服务器IP")
	private String host;
	@FlankerColumn
	@ApiModelProperty("服务端口")
	private int port = 21;
	@FlankerColumn(length = 100)
	@ApiModelProperty("相对路径")
	private String uri;
	@FlankerColumn(length = 50)
	@ApiModelProperty("账号名")
	private String username;
	@FlankerColumn(length = 50)
	@ApiModelProperty("账号密码")
	private String password;

	@Override
	public String toString()
	{
		return this.host + ":" + this.port + "/" + this.uri;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
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

	public String getUri()
	{
		return uri;
	}

	public void setUri(String uri)
	{
		this.uri = uri;
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

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

}
