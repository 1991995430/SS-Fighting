package com.ss.song.xml;

import com.ss.song.annotation.XmlAttribute;
import com.ss.song.annotation.XmlList;
import org.apache.commons.lang.StringUtils;


import java.util.ArrayList;
import java.util.List;

public class XmlNode
{
	@XmlAttribute
	private String host;
	@XmlAttribute
	private String username;
	@XmlAttribute
	private String password;
	@XmlAttribute
	private int port;
	@XmlList(itemClass = XmlAppRef.class, name = "app")
	private final List<XmlAppRef> appRefList = new ArrayList<>();

	/*public static SshContext toSshContext(XmlNode xmlNode)
	{
		SshContext sshContext = new SshContext();
		sshContext.setHost(xmlNode.getHost());
		sshContext.setUsername(xmlNode.getUsername());
		sshContext.setPassword(xmlNode.getPassword());
		sshContext.setPort(xmlNode.getPort());
		return sshContext;
	}

	public static FtpContext toFtpContext(XmlNode xmlNode)
	{
		FtpContext ftpContext = new FtpContext();
		ftpContext.setIpAddress(xmlNode.getHost());
		ftpContext.setUser(xmlNode.getUsername());
		ftpContext.setPassword(xmlNode.getPassword());
		ftpContext.setPort(xmlNode.getPort());
		ftpContext.setSsl(true);
		return ftpContext;
	}*/

	public String getHost()
	{
		return host;
	}

	public String getUsername()
	{
		return username;
	}

	public String getPassword()
	{
		return password;
	}

	public int getPort()
	{
		return port;
	}

	public List<XmlAppRef> getAppRefList()
	{
		return appRefList;
	}

	public boolean containsAppName(String appName)
	{
		for (XmlAppRef xmlAppRef : appRefList)
		{
			if (StringUtils.equals(xmlAppRef.getRef(), appName))
			{
				return true;
			}
		}
		return false;
	}

	/*public boolean isLocalNode()
	{
		String hostname = LocalhostUtils.getHostname();
		String ip = LocalhostUtils.getIp();
		return StringUtils.equalsIgnoreCase(this.host, hostname) || StringUtils.equalsIgnoreCase(this.host, ip);
	}*/

}
