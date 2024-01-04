package com.ss.song.xml;

import com.ss.song.annotation.XmlList;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class XmlCluster
{
	@XmlList(itemClass = XmlNode.class, name = "node")
	private final List<XmlNode> nodeList = new ArrayList<>();

	public List<XmlNode> getNodeList()
	{
		return nodeList;
	}

	public XmlNode findNodeByHostname(String hostname)
	{
		for (XmlNode xmlNode : nodeList)
		{
			if (StringUtils.equalsIgnoreCase(xmlNode.getHost(), hostname))
			{
				return xmlNode;
			}
		}
		return null;
	}

	public XmlNode findLocalNode()
	{
		for (XmlNode node : nodeList)
		{
			/*if (node.isLocalNode())
			{
				return node;
			}*/
		}
		return null;
	}

}
