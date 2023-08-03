package com.ys.authority.controller.menu.param;

import com.ys.authority.mapper.dao.SysMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class MenuAddParams {

    @NotBlank(message = "菜单内容不能为空")
    @ApiModelProperty(value = "菜单项内容", required = true)
    private String menuItem;

    @NotNull(message = "业务ID不能为空")
    @ApiModelProperty(value = "业务ID", required = true)
    private Integer serviceId;

    public SysMenu buildMenuObject() {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setMenuItem(menuItem);
        sysMenu.setServiceId(serviceId);
        return sysMenu;
    }
}
