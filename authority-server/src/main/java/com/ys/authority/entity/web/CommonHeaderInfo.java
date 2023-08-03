package com.ys.authority.entity.web;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CommonHeaderInfo {
    @ApiModelProperty(value = "客户端设备id.设备唯一ID")
    private String cdd;

    @ApiModelProperty(value = "客户端类型（1:android应用, 2:ios应用, 3: 微信公共号 4：微信小程序 5：cms平台 6:pda 9:其他）")
    private String ct;

    @ApiModelProperty(value = "客户端版本,v1.0等，app的版本，安卓和IOS可能不一样")
    private String cv;

    @ApiModelProperty(value = "用户登录token")
    private String tk;

    @ApiModelProperty(value = "客户端IP地址，不填后台会自动获取")
    private String ip;
}