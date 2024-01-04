package com.ss.song.xml;

import com.ss.song.annotation.XmlAttribute;

public class XmlProperty
{
	@XmlAttribute
	private String key;
	@XmlAttribute
	private String value;

	public String getKey()
	{
		return key;
	}

	public String getValue()
	{
		return value;
	}

}
