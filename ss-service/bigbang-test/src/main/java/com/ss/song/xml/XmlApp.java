package com.ss.song.xml;

import com.ss.song.annotation.XmlAttribute;
import com.ss.song.annotation.XmlList;
import com.ss.song.annotation.XmlObject;

import java.util.ArrayList;
import java.util.List;

public class XmlApp
{
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String deployPath;
	@XmlAttribute
	private String version;
	@XmlAttribute
	private Integer dubboPort;
	@XmlAttribute
	private Integer serverPort;
	@XmlAttribute
	private Integer cachePeriodDays;
	@XmlAttribute
	private String plugin;
	/**
	 * 调试端口
	 */
	@XmlAttribute
	private Integer debugPort;
	@XmlAttribute
	private String logLevel = "info";
	@XmlAttribute
	private String sqlLogLevel = "info";
	@XmlObject
	private XmlDataSource datasource;
	@XmlList(name = "property", itemClass = XmlProperty.class)
	private final List<XmlProperty> propertyList = new ArrayList<>();

	public String getName()
	{
		return name;
	}

	public String getDeployPath()
	{
		return deployPath;
	}

	public String getVersion()
	{
		return version;
	}

	public Integer getDubboPort()
	{
		return dubboPort;
	}

	public Integer getServerPort()
	{
		return serverPort;
	}

	public Integer getDebugPort()
	{
		return debugPort;
	}

	public List<XmlProperty> getPropertyList()
	{
		return propertyList;
	}

	public String getPlugin()
	{
		return plugin;
	}

	public void setPlugin(String plugin)
	{
		this.plugin = plugin;
	}

	public String getLogLevel()
	{
		return logLevel;
	}

	public String getSqlLogLevel()
	{
		return sqlLogLevel;
	}

	public XmlDataSource getDatasource()
	{
		return datasource;
	}

	public Integer getCachePeriodDays()
	{
		return cachePeriodDays;
	}

}
