package com.ys.authority.controller.login.vo;

import com.ys.common.utils.RestResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "Token校验结果")
public class TokenCheckResultVO implements Serializable {
    @ApiModelProperty(value = "当前用户信息")
    UserVO user;

    public RestResult<TokenCheckResultVO> success() {
        RestResult<TokenCheckResultVO> RestResult = new RestResult<>();
        RestResult.setData(this);
        return RestResult;
    }

    public RestResult<TokenCheckResultVO> noAuth() {
        RestResult<TokenCheckResultVO> restResult = new RestResult<>();
        restResult.setStatus(RestResult.CODE_STATUS_ERROR);
        restResult.setMessage("Fail to check token.");
        restResult.setData(this);
        return restResult;
    }
}
