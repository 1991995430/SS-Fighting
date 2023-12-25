package com.ss.song.service;

import com.ss.song.meta.TableMeta;
import com.ss.song.utils.DataUtils;
import com.ss.song.utils.TableData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class FlankerRendererService
{
	private final Map<Class<?>, FlankerRenderer<?>> flankerRendererMap = new HashMap<>();

	@Resource
	private FlankerMetaService flankerMetaService;
	@Resource
	private ApplicationContext applicationContext;

	Log log = LogFactory.getLog(getClass());

	@SuppressWarnings("rawtypes")
	protected void init()
	{
		flankerRendererMap.clear();
		Map<String, FlankerRenderer> beanMap = this.applicationContext.getBeansOfType(FlankerRenderer.class);
		for (Map.Entry<String, FlankerRenderer> entry : beanMap.entrySet())
		{
			FlankerRenderer renderer = entry.getValue();
			flankerRendererMap.put(renderer.getTableClass(), renderer);
			log.info("FlankerRenderer tableClass: " + renderer.getTableClass().getName());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void render(Object data)
	{
		if (data == null)
		{
			return;
		}
		TableMeta tableMeta = flankerMetaService.getTableMeta(data.getClass());
		// 深度遍历可渲染对象
		DataUtils.dataStream(new TableData(tableMeta, data)).filter(td -> flankerRendererMap.containsKey(td.getTableClass())).forEach(td ->
		{
			FlankerRenderer renderer = (FlankerRenderer) flankerRendererMap.get(td.getTableClass());
			renderer.render(td.getData());
		});
	}

}
