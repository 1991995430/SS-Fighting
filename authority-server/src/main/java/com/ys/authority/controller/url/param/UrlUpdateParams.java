package com.ys.authority.controller.url.param;

import com.ys.authority.mapper.dao.SysUrl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel
public class UrlUpdateParams implements Serializable {
    @NotNull(message = "ID不能为空")
    @ApiModelProperty(value = "菜单ID", required = true)
    private Integer id;

    @NotBlank(message = "url不能为空")
    @ApiModelProperty(value = "菜单项内容", required = true)
    private String urlItem;

    public SysUrl buildUrlObject() {
        SysUrl sysUrl = new SysUrl();
        sysUrl.setId(id);
        sysUrl.setUrlItem(urlItem);
        return sysUrl;
    }
}
