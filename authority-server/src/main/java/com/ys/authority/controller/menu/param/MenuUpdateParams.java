package com.ys.authority.controller.menu.param;

import com.ys.authority.mapper.dao.SysMenu;
import com.ys.authority.mapper.dao.SysUrl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class MenuUpdateParams {
    @NotNull(message = "ID不能为空")
    @ApiModelProperty(value = "菜单ID", required = true)
    private Integer id;

    @NotBlank(message = "menu不能为空")
    @ApiModelProperty(value = "菜单项内容", required = true)
    private String menuItem;

    public SysMenu buildMenuObject() {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setId(id);
        sysMenu.setMenuItem(menuItem);
        return sysMenu;
    }
}
