package com.ss.song.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 类扫描器
 *
 * @author wang.sheng
 *
 */
public final class ClassScanner
{
	static Log log = LogFactory.getLog(ClassScanner.class);

	public static Stream<Class<?>> scan(String... packages)
	{
		return Arrays.asList(packages).stream().flatMap(packageName -> scanPackage(packageName));
	}

	public static Stream<Class<?>> scanPackage(String packageName)
	{
		String[] path = StringUtils.split(packageName, '.');
		String pattern = "classpath*:" + StringUtils.join(path, "/") + "/**/*.class";
		log.info("class scan pattern: " + pattern);
		Set<Class<?>> classSet = new HashSet<>();
		try
		{
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resolver.getResources(pattern);
			MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resolver);
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			for (Resource resource : resources)
			{
				ClassMetadata classMetadata = readerFactory.getMetadataReader(resource).getClassMetadata();
				if (classMetadata.hasEnclosingClass())
				{
					continue;
				}
				classSet.add(classLoader.loadClass(classMetadata.getClassName()));
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("scanPackage failed! scan package: " + packageName, e);
		}
		return classSet.stream();
	}

	private ClassScanner()
	{
	}
}
