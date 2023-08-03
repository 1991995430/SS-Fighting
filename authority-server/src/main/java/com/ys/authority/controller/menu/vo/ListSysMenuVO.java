package com.ys.authority.controller.menu.vo;

import com.ys.authority.mapper.dao.SysMenu;
import com.ys.common.bean.PageHelperVO;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "Menu询结果")
public class ListSysMenuVO extends PageHelperVO<SysMenu> {
    @ApiModelProperty(value = "菜单列表")
    List<SysMenu> sysMenuList;

    public RestResult<ListSysMenuVO> success() {
        buildPageInfo(sysMenuList);
        RestResult<ListSysMenuVO> result = new RestResult<>();
        result.setData(this);
        return result;
    }
}
