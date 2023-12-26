package com.ss.song.model;

import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

/**
 * 树节点
 *
 * @author wang.sheng
 *
 */
public interface TreeNode<T extends TreeNode<T>>
{
	String ROOT = "ROOT";

	List<T> getChildren();

	String getId();

	String getParentId();

	T parent();

	void parent(T parent);

	/**
	 * 添加孩子节点
	 *
	 * @param child
	 */
	@SuppressWarnings("unchecked")
	default void addChild(T child)
	{
		this.getChildren().add(child);
		child.parent((T) this);
	}

	/**
	 * 删除指定孩子节点
	 *
	 * @param child
	 */
	default void removeChild(T child)
	{
		if (this.getChildren().remove(child))
		{
			child.clear();
		}
	}

	/**
	 * 从上一级节点中删除当前节点
	 */
	@SuppressWarnings("unchecked")
	default void remove()
	{
		if (this.isRoot())
		{
			return;
		}
		this.parent().removeChild((T) this);
	}

	/**
	 * 清除节点父子属性
	 */
	default void clear()
	{
		this.getChildren().clear();
		this.parent(null);
	}

	default boolean isLeaf()
	{
		return this.getChildren().isEmpty();
	}

	default boolean isRoot()
	{
		return this.parent() == null && StringUtils.equals(this.getParentId(), ROOT);
	}

	@SuppressWarnings("unchecked")
	default List<T> path()
	{
		List<T> list = new LinkedList<>();
		T node = (T) this;
		while (node != null)
		{
			list.add(node);
			node = node.parent();
		}
		if (list.size() > 1)
		{
			Collections.reverse(list);
		}
		return list;
	}

	/**
	 * 包含自己在内的节点Stream
	 *
	 * @param recursive
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default Stream<T> nodeStream(boolean recursive)
	{
		List<T> list = new LinkedList<>();
		list.add((T) this);
		if (!recursive)
		{
			// 非递归
			list.addAll(this.getChildren());
		}
		else
		{
			// 递归搜索
			Stack<T> stack = new Stack<>();
			stack.push((T) this);
			while (!stack.isEmpty())
			{
				T node = stack.pop();
				if (!node.isLeaf())
				{
					for (T child : node.getChildren())
					{
						stack.push(child);
						list.add(child);
					}
				}
			}
		}
		return list.stream();
	}
}
