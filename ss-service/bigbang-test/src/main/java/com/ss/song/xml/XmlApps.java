package com.ss.song.xml;

import com.ss.song.annotation.XmlAttribute;
import com.ss.song.annotation.XmlMap;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class XmlApps
{
	@XmlAttribute
	private String zookeeper;
	@XmlAttribute
	private String redis;
	@XmlMap(name = "app", key = "name", valueClass = XmlApp.class)
	private final Map<String, XmlApp> appMap = new HashMap<>();

	public String getZookeeper()
	{
		return zookeeper;
	}

	public ConnectParam getRedis()
	{
		if (StringUtils.isEmpty(redis))
		{
			return null;
		}
		return new ConnectParam(redis);
	}

	public Map<String, XmlApp> getAppMap()
	{
		return appMap;
	}

}
