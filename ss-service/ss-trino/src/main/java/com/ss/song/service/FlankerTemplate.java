package com.ss.song.service;

import com.ss.song.enums.FetchMode;
import com.ss.song.model.Path;
import com.ss.song.model.TreeNode;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Flanker框架的模板接口
 *
 * @author wang.sheng
 *
 */
public interface FlankerTemplate
{
	/**
	 * 根据类型和KEY获取对象
	 *
	 * @param <T>
	 * @param tableClass
	 * @param key
	 * @return
	 */
	<T> T get(Class<T> tableClass, String key);

	/**
	 * 获取对象并克隆(用于修改目的)
	 *
	 * @param <T>
	 * @param tableClass
	 * @param key
	 * @param fetchMode
	 * @return
	 */
	default <T> T get(Class<T> tableClass, String key, FetchMode fetchMode)
	{
		T data = this.get(tableClass, key);
		if (fetchMode == FetchMode.CLONED)
		{
			return FlankerUtils.clone(tableClass, data);
		}
		return data;
	}

	/**
	 * 获取对象中定义的KEY值
	 *
	 * @param data
	 * @return
	 */
	String getKey(Object data);

	/**
	 * 设置指定对象的KEY值
	 *
	 * @param data
	 * @param key
	 */
	void setKey(Object data, String key);

	/**
	 * 查询指定的一条记录.如不存在则返回null,超过1条的则抛出异常
	 *
	 * @param <T>
	 * @param tableClass
	 * @param filter
	 * @return
	 */
	<T> T queryForObject(Class<T> tableClass, FlankerFilter<T> filter);

	/**
	 * 查询指定一条记录并克隆
	 *
	 * @param <T>
	 * @param tableClass
	 * @param filter
	 * @param fetchMode
	 * @return
	 */
	default <T> T queryForObject(Class<T> tableClass, FlankerFilter<T> filter, FetchMode fetchMode)
	{
		T data = this.queryForObject(tableClass, filter);
		if (fetchMode == FetchMode.CLONED)
		{
			return FlankerUtils.clone(tableClass, data);
		}
		return data;
	}

	/**
	 * 重新加载指定路径上的缓存对象
	 *
	 * @param path
	 * @throws
	 */
	void reload(Path path);
	/**
	 * 删除指定表主键对应的记录
	 *
	 * @param tableClass
	 * @param key
	 * @throws
	 */
	void delete(Class<?> tableClass, String key) ;
	/**
	 * 将对象覆盖到缓存中并同步至DB
	 *
	 * @param
	 * @throws
	 */
	void overwrite(Object data) ;
	/**
	 * 插入一个根对像
	 *
	 * @param rootData
	 * @throws
	 */
	void insertRoot(Object rootData);
	/**
	 * 在指定路径的对象内部插入一个常规对象
	 *
	 * @param path
	 * @param data
	 * @throws
	 */
	void insert(Path path, Object data) ;

	/**
	 * 流式查询
	 *
	 * @param <T>
	 * @param tableClass
	 * @param filter
	 * @param comparator
	 * @return
	 */
	<T> Stream<T> queryForStream(Class<T> tableClass, FlankerFilter<T> filter, Comparator<T> comparator);

	/**
	 * 获取对象流并用于修改的目的
	 *
	 * @param <T>
	 * @param tableClass
	 * @param filter
	 * @param comparator
	 * @param fetchMode
	 * @return
	 */
	default <T> Stream<T> queryForStream(Class<T> tableClass, FlankerFilter<T> filter, Comparator<T> comparator, FetchMode fetchMode)
	{
		Stream<T> stream = this.queryForStream(tableClass, filter, comparator);
		if (fetchMode == FetchMode.CLONED)
		{
			return stream.map(data -> FlankerUtils.clone(tableClass, data));
		}
		return stream;
	}

	/**
	 * 列表查询
	 *
	 * @param <T>
	 * @param tableClass
	 * @param filter
	 * @param comparator
	 * @return
	 */
	default <T> List<T> queryForList(Class<T> tableClass, FlankerFilter<T> filter, Comparator<T> comparator)
	{
		return this.queryForStream(tableClass, filter, comparator).collect(Collectors.toList());
	}

	/**
	 * 列表查询并用于修改的目的
	 *
	 * @param <T>
	 * @param tableClass
	 * @param filter
	 * @param comparator
	 * @param fetchMode
	 * @return
	 */
	default <T> List<T> queryForList(Class<T> tableClass, FlankerFilter<T> filter, Comparator<T> comparator, FetchMode fetchMode)
	{
		return this.queryForStream(tableClass, filter, comparator, fetchMode).collect(Collectors.toList());
	}

	/**
	 * 分页查询
	 *
	 * @param <T>
	 * @param tableClass
	 * @param filter
	 * @param comparator
	 * @param start
	 * @param limit
	 * @return
	 */
	//<T> PagingResult<T> queryForPagingResult(Class<T> tableClass, FlankerFilter<T> filter, Comparator<T> comparator, int start, int limit);

	/**
	 * 获得树结构集合
	 *
	 * @param <T>
	 * @param tableClass
	 * @return
	 */
	<T extends TreeNode<T>> List<T> getTreeList(Class<T> tableClass);

}
