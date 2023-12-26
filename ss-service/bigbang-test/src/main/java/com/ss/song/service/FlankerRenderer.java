package com.ss.song.service;

/**
 * 渲染器
 *
 * @author wang.sheng
 *
 */
public interface FlankerRenderer<T>
{
	Class<T> getTableClass();

	/**
	 * 对指定对象进行快速渲染
	 *
	 * @param data
	 */
	void render(T data);
}
