package com.ss.song.utils;

import com.alibaba.fastjson2.JSON;

/**
 * Flanker框架的工具包
 *
 * @author wang.sheng
 *
 */
final class FlankerUtils
{

	/**
	 * 深度克隆一个对象
	 *
	 * @param <T>
	 * @param tableClass
	 * @param data
	 * @return
	 */
	public static <T> T clone(Class<T> tableClass, Object data)
	{
		if (data == null)
		{
			return null;
		}
		String json = JSON.toJSONString(data);
		return JSON.parseObject(json, tableClass);
	}

	private FlankerUtils()
	{
	}
}
