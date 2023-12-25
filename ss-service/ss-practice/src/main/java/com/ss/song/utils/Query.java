package com.ss.song.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ss.song.meta.ColumnMeta;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public final class Query
{
	private final List<Object> paramList = new ArrayList<>();
	private String sql;

	public Query(String sql, Object... params)
	{
		this.sql = sql;
		if (params != null && params.length > 0)
		{
			for (Object param : params)
			{
				paramList.add(param);
			}
		}
	}

	public Query(String sql, List<Object> paramList)
	{
		this.sql = sql;
		if (!CollectionUtils.isEmpty(paramList))
		{
			this.paramList.addAll(paramList);
		}
	}

	public Query()
	{
	}

	public Query clear()
	{
		this.sql = null;
		this.paramList.clear();
		return this;
	}

	public Query setSql(String sql)
	{
		this.sql = sql;
		return this;
	}

	public String getSql()
	{
		return sql;
	}

	public Object[] getParams()
	{
		return paramList.toArray();
	}

	public Query appendKey(String str)
	{
		if (StringUtils.isEmpty(str))
		{
			throw new IllegalArgumentException("key cannot null or empty!");
		}
		paramList.add(str);
		return this;
	}

	public Query append(ColumnMeta columnMeta, Object value)
	{
		if (value == null)
		{
			if (!columnMeta.isNullable())
			{
				// 取值为null但字段不可NULL

			}
			paramList.add(null);
			return this;
		}
		Class<?> dataType = value.getClass();
		switch (columnMeta.getSqlType())
		{
		case VARCHAR:
			if (dataType == String.class)
			{
				// 字符串或枚举
				paramList.add(value.toString());
			}
			else if (dataType.isEnum())
			{
				// 枚举类型
				Enum<?> e = (Enum<?>) value;
				paramList.add(e.name());
			}
			else if (EnumSet.class.isAssignableFrom(dataType))
			{
				// 枚举集合类型
				EnumSet<?> enumSet = (EnumSet<?>) value;
				List<String> nameList = enumSet.stream().map(e -> e.name()).collect(Collectors.toList());
				paramList.add(StringUtils.join(nameList, ","));
			}
			else if (Collection.class.isAssignableFrom(dataType))
			{
				// 集合类型
				Collection<?> collection = (Collection<?>) value;
				paramList.add(StringUtils.join(collection, ","));
			}
			else
			{
				// 不识别的数据类型

			}
			break;
		case TEXT:
		case LONGTEXT:
			if (dataType == String.class)
			{
				paramList.add(value.toString());
			}
			else if (Collection.class.isAssignableFrom(dataType))
			{
				// 集合类型
				Collection<?> collection = (Collection<?>) value;
				paramList.add(StringUtils.join(collection, ","));
			}
			else if (dataType == JSONObject.class)
			{
				// 转化为JSON字符串
				paramList.add(JSON.toJSONString(value));
			}
			else
			{
				// 不识别的数据类型

			}
			break;
		case BLOB:
		case LONGBLOB:
			if (dataType == byte[].class)
			{
				paramList.add(value);
			}
			else
			{

			}
			break;
		default:
			paramList.add(value);
			break;
		}
		return this;
	}

	@Override
	public String toString()
	{
		return new StringBuffer().append("sql: ").append(this.sql).append("; params: [").append(StringUtils.join(paramList, "; ")).append("]").toString();
	}

}
