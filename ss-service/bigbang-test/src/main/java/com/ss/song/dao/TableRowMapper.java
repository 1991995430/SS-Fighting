package com.ss.song.dao;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ss.song.meta.ColumnMeta;
import com.ss.song.meta.TableMeta;
import com.ss.song.model.IndexedData;
import com.ss.song.utils.ReflectUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

final class TableRowMapper implements RowMapper<IndexedData>
{
	static Log log = LogFactory.getLog(TableRowMapper.class);

	private static byte[] toByteArray(Blob blob)
	{
		InputStream is = null;
		try
		{
			is = blob.getBinaryStream();
			return IOUtils.toByteArray(is);
		}
		catch (Exception e)
		{
			log.warn("toByteArray failed!", e);
			return new byte[0];
		}
		finally
		{
			IOUtils.closeQuietly(is);
		}
	}

	private TableMeta tableMeta;

	public TableRowMapper(TableMeta tableMeta)
	{
		this.tableMeta = tableMeta;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public IndexedData mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		// 通过反射创建对象
		IndexedData indexedData = new IndexedData(tableMeta.newDataInstance());
		String key = rs.getString(tableMeta.getKeyMeta().getName());
		ReflectUtils.setFieldValue(indexedData.getRaw(), tableMeta.getKeyMeta().getField(), key);
		if (!tableMeta.isRoot())
		{
			// 非根表
			for (TableMeta parentTableMeta : tableMeta.getParents())
			{
				// 设置外键索引
				String foreignKey = rs.getString(parentTableMeta.foreignKeyName());
				indexedData.getPath().append(parentTableMeta.getTableClass(), foreignKey);
			}
		}
		for (ColumnMeta column : tableMeta.getColumns())
		{
			Object value = null;
			Field field = column.getField();
			switch (column.getSqlType())
			{
			case INT:
				value = rs.getInt(column.getName());
				break;
			case BIGINT:
				value = rs.getLong(column.getName());
				break;
			case DECIMAL:
				value = rs.getBigDecimal(column.getName());
				break;
			case BOOLEAN:
				value = rs.getBoolean(column.getName());
				break;
			case BLOB:
			case LONGBLOB:
				Blob blob = rs.getBlob(column.getName());
				value = toByteArray(blob);
				break;
			default:
				String str = rs.getString(column.getName());
				Class<?> dataType = field.getType();
				if (dataType == String.class)
				{
					// 字符串
					value = str;
				}
				else if (dataType == Set.class)
				{
					// 集合
					String[] array = StringUtils.split(str, ',');
					if (array != null)
					{
						Set<String> set = new TreeSet<>();
						for (String item : array)
						{
							set.add(StringUtils.trim(item));
						}
						value = set;
					}
				}
				else if (dataType == List.class)
				{
					// 列表
					String[] array = StringUtils.split(str, ',');
					if (array != null)
					{
						List<String> list = new ArrayList<>();
						for (String item : array)
						{
							list.add(StringUtils.trim(item));
						}
						value = list;
					}
				}
				else if (dataType.isEnum())
				{
					// 枚举
					if (!StringUtils.isEmpty(str))
					{
						value = Enum.valueOf(dataType.asSubclass(Enum.class), str);
					}
					else
					{
						value = null;
					}
				}
				else if (dataType == EnumSet.class)
				{
					// 枚举集合
					String[] array = StringUtils.split(str, ',');
					if (array != null)
					{
						EnumSet enumSet = EnumSet.noneOf(column.getEnumType());
						for (String name : array)
						{
							enumSet.add(Enum.valueOf(column.getEnumType(), StringUtils.trim(name)));
						}
						value = enumSet;
					}
				}
				else if (dataType == JSONObject.class)
				{
					if (!StringUtils.isEmpty(str))
					{
						value = JSON.parseObject(str);
					}
				}
				else
				{
					// 不识别的字段类型
					//throw new StandardRuntimeException(FlankerErrorType.Query.UNKNOWN_FIELD_TYPE, field.getType().getName());
				}
				break;
			}
			if (value != null)
			{
				ReflectUtils.setFieldValue(indexedData.getRaw(), column.getField(), value);
			}
		}
		return indexedData;
	}

}
