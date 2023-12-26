package com.ss.song.service;

import com.ss.song.dao.FlankerJdbcTemplate;
import com.ss.song.dao.RootDataDao;
import com.ss.song.meta.TableMeta;
import com.ss.song.model.Indexed;
import com.ss.song.model.Path;
import com.ss.song.utils.ReflectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * 根数据对象高速缓存管理器
 *
 * @author wang.sheng
 *
 */
@Service
public class FlankerCache
{
	/**
	 * 全部对象进行管理的高速缓存
	 */
	private final Map<Class<?>, Map<String, Object>> rootDataMapCache = new HashMap<>();

	@Resource
	private RootDataDao rootDataDao;
	@Resource
	private FlankerMetaService flankerMetaService;
	@Resource
	private FlankerJdbcTemplate flankerJdbcTemplate;
	@Resource
	private FlankerRendererService flankerRendererService;
	@Value("${flanker.packages:com.ss.song}")
	private String packages;

	Log log = LogFactory.getLog(getClass());

	/**
	 * 重新完全加载根数据
	 */
	@PostConstruct
	@Scheduled(cron = "0 0 2 * * ?")
	protected void reload()
	{
		try
		{
			// 清空数据缓存
			this.rootDataMapCache.values().stream().forEach(map -> map.clear());
			this.rootDataMapCache.clear();
			this.flankerMetaService.scan(StringUtils.split(this.packages, ","));
			long t0 = System.currentTimeMillis();
			for (TableMeta rootTableMeta : flankerMetaService.getRootTableMetas())
			{
				if (!rootTableMeta.isCached())
				{
					// 无需缓存
					continue;
				}
				Class<?> tableClass = rootTableMeta.getTableClass();
				log.info("start load table datas: " + rootTableMeta.getName());
				long t1 = System.currentTimeMillis();
				List<?> rootDataList = this.rootDataDao.loadAll(tableClass);
				Map<String, Object> rootDataMap = new ConcurrentHashMap<>();
				for (Object rootData : rootDataList)
				{
					String rootKey = (String) ReflectUtils.getFieldValue(rootData, rootTableMeta.getKeyMeta().getField());
					rootDataMap.put(rootKey, rootData);
				}
				rootDataMapCache.put(tableClass, rootDataMap);
				long t2 = System.currentTimeMillis();
				log.info("table: " + rootTableMeta.getName() + " datas loaded successful! use time: " + (t2 - t1) + "ms.");
			}
			long t3 = System.currentTimeMillis();
			log.info("all root table loaded successful! use time: " + (t3 - t0) + "ms.");
			// 渲染所有的根对像
			new Thread(() ->
			{
				try
				{
					Thread.sleep(5000L);
				}
				catch (InterruptedException e)
				{
				}
				flankerRendererService.init();
				for (Map.Entry<Class<?>, Map<String, Object>> entry : rootDataMapCache.entrySet())
				{
					// 深度渲染根对像
					for (Object data : entry.getValue().values())
					{
						flankerRendererService.render(data);
					}
				}
			}, "flankerRendererService-init").start();
		}
		catch (Exception e)
		{
			log.error("reload failed!", e);
		}
	}

	/**
	 * 从缓存中获取指定根对像
	 *
	 * @param <T>
	 * @param tableClass
	 * @param key
	 * @return
	 */
	protected <T> T getRoot(Class<T> tableClass, String key)
	{
		Map<String, Object> dataMap = rootDataMapCache.get(tableClass);
		if (dataMap == null || !dataMap.containsKey(key))
		{
			return null;
		}
		return tableClass.cast(dataMap.get(key));
	}

	/**
	 * 是否存在根对像
	 *
	 * @param tableClass
	 * @param key
	 * @return
	 */
	protected boolean existsRoot(Class<?> tableClass, String key)
	{
		Map<String, Object> dataMap = rootDataMapCache.get(tableClass);
		return dataMap != null && dataMap.containsKey(key);
	}

	/**
	 * 根据路径获得缓存数据对象
	 *
	 * @param path
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object find(Path path)
	{
		if (path == null || path.isEmpty())
		{
			throw new IllegalArgumentException("path cannot null or empty!");
		}
		Iterator<Indexed> iterator = path.iterator();
		Indexed firstIndexed = iterator.next();
		Object data = this.getRoot(firstIndexed.getTableClass(), firstIndexed.getKey());
		if (data == null)
		{
			// 根对像不存在
			return null;
		}
		while (iterator.hasNext())
		{
			Indexed indexed = iterator.next();
			TableMeta tableMeta = this.flankerMetaService.getTableMeta(indexed.getTableClass());
			Object fieldValue = ReflectUtils.getFieldValue(data, tableMeta.getParentTableField());
			if (fieldValue == null)
			{
				// 线索中断,返回null
				return null;
			}
			if (tableMeta.isSingleton())
			{
				// 单例
				String key = (String) ReflectUtils.getFieldValue(fieldValue, tableMeta.getKeyMeta().getField());
				if (StringUtils.equals(key, indexed.getKey()) && fieldValue.getClass() == indexed.getTableClass())
				{
					// 匹配上,进入下一循环
					data = fieldValue;
				}
				else
				{
					// 未匹配,则结束返回null
					return null;
				}
			}
			else
			{
				// 多实例
				List list = (List) fieldValue;
				if (CollectionUtils.isEmpty(list))
				{
					// 集合为空
					return null;
				}
				boolean found = false;
				for (Object childData : list)
				{
					String key = (String) ReflectUtils.getFieldValue(childData, tableMeta.getKeyMeta().getField());
					if (StringUtils.equals(key, indexed.getKey()) && childData.getClass() == indexed.getTableClass())
					{
						// 匹配上,进入下一循环
						data = childData;
						found = true;
						break;
					}
				}
				if (!found)
				{
					// 无法匹配
					return null;
				}
			}
		}
		return data;
	}

	/**
	 * 添加根对像
	 *
	 * @param rootData
	 */
	public void insertRoot(Object rootData)
	{
		Class<?> tableClass = rootData.getClass();
		TableMeta tableMeta = this.flankerMetaService.getRootTableMeta(tableClass);
		String rootKey = rootDataDao.insert(rootData);
		if (tableMeta.isCached())
		{
			// 需进行缓存
			Map<String, Object> dataMap = rootDataMapCache.get(tableClass);
			if (dataMap == null)
			{
				//throw new StandardException(FlankerErrorType.Metadata.TABLE_NOT_EXISTS, tableClass.getName());
			}
			dataMap.put(rootKey, rootData);
		}
	}

	/**
	 * 获取指定类型的数据流
	 *
	 * @param <T>
	 * @param tableClass
	 * @return
	 */
	public  <T> Stream<T> getRootStream(Class<T> tableClass)
	{
		Map<String, Object> dataMap = rootDataMapCache.get(tableClass);
		List<Object> list = new LinkedList<>();
		if (dataMap != null && !dataMap.isEmpty())
		{
			list.addAll(dataMap.values());
		}
		return list.stream().map(data -> tableClass.cast(data));
	}

	/**
	 * 从缓存中删除对象并同步
	 *
	 * @param tableClass
	 * @param key
	 */
	public void deleteRoot(Class<?> tableClass, String key)
	{
		Map<String, Object> dataMap = rootDataMapCache.get(tableClass);
		if (dataMap == null)
		{
			//throw new StandardException(FlankerErrorType.Metadata.TABLE_NOT_EXISTS, tableClass.getName());
		}
		if (dataMap.containsKey(key))
		{
			Object data = dataMap.get(key);
			synchronized (data)
			{
				rootDataDao.delete(data);
				dataMap.remove(key);
			}
		}
	}
	/**
	 * 重新加载根节点
	 *
	 * @param <T>
	 * @param tableClass
	 * @param key
	 */
	public  <T> T reloadRoot(Class<T> tableClass, String key)
	{
		Map<String, Object> dataMap = rootDataMapCache.get(tableClass);
		if (dataMap == null)
		{
			//throw new StandardException(FlankerErrorType.Metadata.TABLE_NOT_EXISTS, tableClass.getName());
		}
		T rootData = this.rootDataDao.load(tableClass, key);
		if (rootData == null)
		{
			// 根对象不存在,与DB不同步,属于脏数据
			dataMap.remove(key);
			return null;
		}
		else
		{
			dataMap.put(key, rootData);
			return rootData;
		}
	}

}
