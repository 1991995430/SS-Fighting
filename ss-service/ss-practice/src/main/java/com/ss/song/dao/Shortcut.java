package com.ss.song.dao;

import com.ss.song.annotation.FlankerColumn;
import com.ss.song.annotation.FlankerKey;
import com.ss.song.annotation.FlankerRelation;
import com.ss.song.annotation.FlankerTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 快捷功能入口
 *
 * @author wang.sheng
 *
 */
@FlankerTable
@ApiModel("快捷入口")
public class Shortcut implements Serializable, Comparable<Shortcut>
{
	private static final long serialVersionUID = 1L;

	@FlankerKey
	@ApiModelProperty("快捷功能ID")
	private String id;
	@FlankerColumn(length = 50, nullable = false)
	@ApiModelProperty("名称")
	private String name;
	@FlankerColumn(length = 50, nullable = false)
	@ApiModelProperty("图标名")
	private String icon;
	@FlankerColumn(length = 50, nullable = false)
	@ApiModelProperty("图标背景色")
	private String color;
	@FlankerColumn(length = 50, nullable = false)
	@ApiModelProperty("是否可见参数名")
	private String visible;
	@FlankerColumn(length = 200)
	@ApiModelProperty("查询消息数量接口URI")
	private String messageCountUri;
	@FlankerColumn
	@ApiModelProperty("序号")
	private int sortIndex;
	@FlankerRelation(target = Role.class)
	private Set<String> roleIdSet;
	private final Set<String> roleNameSet = new HashSet<>();
	@ApiModelProperty("新消息数量,用于前端展示")
	private int messageCount;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
	}

	public String getVisible()
	{
		return visible;
	}

	public void setVisible(String visible)
	{
		this.visible = visible;
	}

	public int getSortIndex()
	{
		return sortIndex;
	}

	public void setSortIndex(int sortIndex)
	{
		this.sortIndex = sortIndex;
	}

	public int getMessageCount()
	{
		return messageCount;
	}

	public void setMessageCount(int messageCount)
	{
		this.messageCount = messageCount;
	}

	public String getMessageCountUri()
	{
		return messageCountUri;
	}

	public void setMessageCountUri(String messageCountUri)
	{
		this.messageCountUri = messageCountUri;
	}

	public Set<String> getRoleIdSet()
	{
		return roleIdSet;
	}

	public void setRoleIdSet(Set<String> roleIdSet)
	{
		this.roleIdSet = roleIdSet;
	}

	public Set<String> getRoleNameSet()
	{
		return roleNameSet;
	}

	@Override
	public int compareTo(Shortcut o)
	{
		return this.sortIndex - o.sortIndex;
	}

	public boolean authorized(Set<String> roleIdSet)
	{
		return !CollectionUtils.isEmpty(this.roleIdSet) && !Collections.disjoint(roleIdSet, this.roleIdSet);
	}

}
