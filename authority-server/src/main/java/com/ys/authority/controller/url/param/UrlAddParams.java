package com.ys.authority.controller.url.param;

import com.ys.authority.mapper.dao.SysUrl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UrlAddParams {
    @NotNull(message = "业务ID不能为空")
    @ApiModelProperty(value = "业务ID", required = true)
    private Integer serviceId;

    @NotBlank(message = "URL内容不能为空")
    @ApiModelProperty(value = "菜单项内容", required = true)
    private String urlItem;

    public SysUrl buildUrlObject() {
        SysUrl sysUrl = new SysUrl();
        sysUrl.setServiceId(serviceId);
        sysUrl.setUrlItem(urlItem);
        return sysUrl;
    }
}
