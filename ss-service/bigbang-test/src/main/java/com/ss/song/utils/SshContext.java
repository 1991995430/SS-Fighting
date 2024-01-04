package com.ss.song.utils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * SSH连接的上下文
 *
 * @author wang.sheng
 *
 */
public class SshContext implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String host;
	private String username;
	private String password;
	private int port = 22;
	private int timeout = 10000;
	private List<String> identityList;
	private Map<String, String> configMap;

	public int getTimeout()
	{
		return timeout;
	}

	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
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

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public List<String> getIdentityList()
	{
		return identityList;
	}

	public void setIdentityList(List<String> identityList)
	{
		this.identityList = identityList;
	}

	public Map<String, String> getConfigMap()
	{
		return configMap;
	}

	public void setConfigMap(Map<String, String> configMap)
	{
		this.configMap = configMap;
	}

}
