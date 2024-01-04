package com.ss.song.utils;

import org.apache.commons.beanutils.ConvertUtils;


/**
 * 类型转换器
 *
 * @author wang.sheng
 *
 */
public final class MyConvertUtils
{
	private MyConvertUtils()
	{
	}

	/**
	 * 将字符串转换为指定对象
	 *
	 * @param value
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convert(String value, Class<T> clazz)
	{
		return (T) ConvertUtils.convert(value, clazz);
	}
}
