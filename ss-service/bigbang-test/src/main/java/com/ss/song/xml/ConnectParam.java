package com.ss.song.xml;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * 连接参数
 *
 * @author wang.sheng
 *
 */
public final class ConnectParam
{
	private String host;
	private int port;
	private String password;

	public ConnectParam(String param)
	{
		if (StringUtils.isEmpty(param))
		{
			throw new IllegalArgumentException("param cannot null or empty!");
		}
		String[] array = StringUtils.split(param, "/");
		if (array.length != 3)
		{
			throw new IllegalArgumentException("invalid param: " + param);
		}
		this.host = array[0];
		this.port = NumberUtils.toInt(array[1]);
		this.password = array[2];
	}

	public String getHost()
	{
		return host;
	}

	public int getPort()
	{
		return port;
	}

	public String getPassword()
	{
		return password;
	}

}
