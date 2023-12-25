package com.ss.song.dao;
import com.ss.song.annotation.FlankerChildren;
import com.ss.song.annotation.FlankerColumn;
import com.ss.song.annotation.FlankerKey;
import com.ss.song.annotation.FlankerTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;

import java.util.EnumSet;
import java.util.List;

@FlankerTable
@ApiModel("试验详情")
public class ComacTest
{
	private static final long serialVersionUID = 1L;

	@FlankerKey
	@ApiModelProperty("试验实例主键ID")
	private String id;
	@FlankerColumn(length = 50, nullable = false)
	@ApiModelProperty("试验编号")
	private String code;
	@FlankerColumn(length = 50, nullable = false)
	@ApiModelProperty("试验类型")
	private String type;
	@FlankerColumn(length = 100, nullable = false)
	@ApiModelProperty("试验名称")
	private String name;
	@FlankerColumn(length = 100, nullable = false)
	@ApiModelProperty("试验室")
	private String department;
	@FlankerColumn(length = 100, nullable = false)
	@ApiModelProperty("控制设备")
	private String device;
	@FlankerColumn
	@ApiModelProperty("试验信息创建时间(绝对毫秒)")
	private long createTime = System.currentTimeMillis();
	@FlankerColumn(length = 50)
	@ApiModelProperty("试验人员")
	private String creator;

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public List<OfflineResource> getOfflineResourceList() {
		return offlineResourceList;
	}

	public void setOfflineResourceList(List<OfflineResource> offlineResourceList) {
		this.offlineResourceList = offlineResourceList;
	}

	@FlankerChildren(childClass = OfflineResource.class)
	@ApiModelProperty("离线数据采集资源列表")
	private List<OfflineResource> offlineResourceList;

	public ComacTest() {
	}

	public boolean filter(String queryString)
	{
		if (StringUtils.isEmpty(queryString))
		{
			return true;
		}
		return StringUtils.contains(this.code, queryString) || StringUtils.contains(this.name, queryString) || StringUtils.contains(this.department, queryString) || StringUtils.contains(this.device, queryString);
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDepartment()
	{
		return department;
	}

	public void setDepartment(String department)
	{
		this.department = department;
	}

	public String getDevice()
	{
		return device;
	}

	public void setDevice(String device)
	{
		this.device = device;
	}
}
