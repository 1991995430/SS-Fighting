package com.ys.authority.controller.login.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "登录结果数据")
public class LoginResultVO implements Serializable {

    @ApiModelProperty(value = "用户名")
    String userName;

    @ApiModelProperty(value = "电话")
    String mobileNumber;

    @ApiModelProperty(value = "邮箱")
    String email;

    @ApiModelProperty(value = "生成的access token信息")
    @JsonProperty("access-token")
    String accessToken;

    @ApiModelProperty(value = "用户角色id")
    List <Integer> roleIdList;

    @ApiModelProperty(value = "用户业务id")
    Integer serviceId;
    @ApiModelProperty(value = "用户部门id")
    Integer deptId;
    @ApiModelProperty(value = "用户部门编码")
    String deptCode;

    @ApiModelProperty(value = "用户id")
    Integer  id;

    public RestResult<LoginResultVO> success() {
        RestResult<LoginResultVO> result = new RestResult<>();
        result.setData(this);
        return result;
    }

    public RestResult<LoginResultVO> error(int status, String message) {
        RestResult<LoginResultVO> result = new RestResult<>();
        result.setStatus(status);
        result.setMessage(message);
        result.setData(this);
        return result;
    }
}
