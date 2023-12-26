package com.ss.song.model;

/**
 * 携带外键索引信息的表数据对象
 *
 * @author wang.sheng
 *
 */
public final class IndexedData
{
	private final Path path = new Path();
	private Object raw;

	public IndexedData(Object raw)
	{
		if (raw == null)
		{
			throw new IllegalArgumentException("raw cannot null!");
		}
		this.raw = raw;
	}

	public IndexedData(Object raw, Path path)
	{
		this(raw);
		this.path.append(path);
	}

	public Path getPath()
	{
		return this.path;
	}

	public Object getRaw()
	{
		return raw;
	}

	@Override
	public String toString()
	{
		return new StringBuffer().append("index: {").append(this.path).append("} raw: {").append(this.raw).append("}").toString();
	}

}
