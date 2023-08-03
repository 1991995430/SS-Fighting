package com.ys.authority.controller.role.vo;

import com.ys.authority.mapper.dao.SysRole;
import com.ys.common.bean.PageHelperVO;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "角色查询结果")
public class ListRoleVO extends PageHelperVO<SysRole> {
    @ApiModelProperty(value = "菜单列表")
    List<SysRole> sysRoleList;

    public RestResult<ListRoleVO> success() {
        buildPageInfo(sysRoleList);
        RestResult<ListRoleVO> result = new RestResult<>();
        result.setData(this);
        return result;
    }
}
