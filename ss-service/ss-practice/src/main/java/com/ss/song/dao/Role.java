package com.ss.song.dao;

import com.ss.song.annotation.FlankerColumn;
import com.ss.song.annotation.FlankerKey;
import com.ss.song.annotation.FlankerTable;
import com.ss.song.enums.GeneratorType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 角色
 *
 * @author wang.sheng
 *
 */
@FlankerTable
@ApiModel("角色")
public class Role implements Serializable, Comparable<Role>
{
	private static final long serialVersionUID = 1L;

	@FlankerKey(length = 50, generatorType = GeneratorType.ASSIGNED)
	@ApiModelProperty("角色ID")
	private String id;
	@FlankerColumn(length = 200)
	@ApiModelProperty("角色名称")
	private String name;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	@Override
	public int compareTo(Role o)
	{
		return this.id.compareTo(o.id);
	}

}
