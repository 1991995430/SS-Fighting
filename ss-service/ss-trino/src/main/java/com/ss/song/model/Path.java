package com.ss.song.model;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 从根对象出发的路径
 *
 * @author wang.sheng
 *
 */
public final class Path
{
	private final List<Indexed> indexedList = new ArrayList<>();

	public Path()
	{
	}

	public Path(Class<?> tableClass, String key)
	{
		this.append(tableClass, key);
	}

	public Path(List<Indexed> indexedList)
	{
		this.indexedList.addAll(indexedList);
	}

	public Path(Path otherPath)
	{
		this.append(otherPath);
	}

	public boolean isEmpty()
	{
		return this.indexedList.isEmpty();
	}

	public Iterator<Indexed> iterator()
	{
		return this.indexedList.iterator();
	}

	public boolean contains(Path otherPath)
	{
		if (this.equals(otherPath))
		{
			return true;
		}
		if (this.size() <= otherPath.size())
		{
			return false;
		}
		for (int i = 0; i < otherPath.indexedList.size(); i++)
		{
			Indexed indexed = this.indexedList.get(i);
			Indexed otherIndexed = otherPath.indexedList.get(i);
			if (!indexed.equals(otherIndexed))
			{
				return false;
			}
		}
		return true;
	}

	public int size()
	{
		return this.indexedList.size();
	}

	/**
	 * 获取上一级路径
	 *
	 * @return
	 */
	public Path getParent()
	{
		// 父对象的路径
		if (this.indexedList.size() <= 1)
		{
			return null;
		}
		return new Path(indexedList.subList(0, this.indexedList.size() - 1));
	}

	public Indexed first()
	{
		if (indexedList.isEmpty())
		{
			return null;
		}
		return indexedList.get(0);
	}

	public Indexed last()
	{
		if (indexedList.isEmpty())
		{
			return null;
		}
		return indexedList.get(indexedList.size() - 1);
	}

	public Path append(Path path)
	{
		if (path == null)
		{
			throw new IllegalArgumentException("path cannot null!");
		}
		for (Indexed indexed : path.indexedList)
		{
			this.append(indexed);
		}
		return this;
	}

	public boolean containsTableClass(Class<?> tableClass)
	{
		for (Indexed indexed : this.indexedList)
		{
			if (indexed.getTableClass() == tableClass)
			{
				return true;
			}
		}
		return false;
	}

	private void clean(Class<?> tableClass)
	{
		Iterator<Indexed> iterator = indexedList.iterator();
		while (iterator.hasNext())
		{
			Indexed indexed = iterator.next();
			if (indexed.getTableClass() == tableClass)
			{
				iterator.remove();
			}
		}
	}

	public Path append(Indexed indexed)
	{
		this.clean(indexed.getTableClass());
		this.indexedList.add(indexed);
		return this;
	}

	public Path append(Class<?> tableClass, String key)
	{
		this.clean(tableClass);
		this.indexedList.add(new Indexed(tableClass, key));
		return this;
	}

	public String getKey(Class<?> tableClass)
	{
		Indexed indexed = this.get(tableClass);
		return indexed != null ? indexed.getKey() : null;
	}

	public Indexed get(Class<?> tableClass)
	{
		for (Indexed indexed : this.indexedList)
		{
			if (indexed.getTableClass() == tableClass)
			{
				return indexed;
			}
		}
		return null;
	}

	public Indexed[] toArray()
	{
		return this.indexedList.toArray(new Indexed[0]);
	}

	public void clear()
	{
		this.indexedList.clear();
	}

	@Override
	public String toString()
	{
		return new StringBuffer().append("[").append(StringUtils.join(this.indexedList, ", ")).append("]").toString();
	}

	@Override
	public int hashCode()
	{
		return this.indexedList.hashCode();
	}

	@Override
	public boolean equals(Object other)
	{
		if (other == null || !(other instanceof Path))
		{
			return false;
		}
		Path otherPath = (Path) other;
		if (this.indexedList.size() != otherPath.indexedList.size())
		{
			return false;
		}
		for (int i = 0; i < this.indexedList.size(); i++)
		{
			Indexed a = this.indexedList.get(i);
			Indexed b = otherPath.indexedList.get(i);
			if (!a.equals(b))
			{
				return false;
			}
		}
		return true;
	}
}
