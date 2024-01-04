package com.ss.song.xml;

import com.ss.song.utils.XmlParseUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class XmlConfiguration
{
	//private static final String XML_CLUSTER_PATH = "conf/cluster.xml";
	private static final String XML_CLUSTER_PATH = "D:\\Project\\SS-Fighting\\ss-service\\bigbang-test\\src\\main\\resources\\cluster.xml";
	//private static final String XML_APPS_PATH = "conf/apps.xml";

	Log log = LogFactory.getLog(getClass());

	@Bean
	public XmlCluster xmlCluster()
	{
		try
		{
			return XmlParseUtils.parseObject(XmlCluster.class, new File(XML_CLUSTER_PATH));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/*@Bean
	public XmlApps xmlApps()
	{
		try
		{
			return XmlParseUtils.parseObject(XmlApps.class, new File(XML_APPS_PATH));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}*/
}
