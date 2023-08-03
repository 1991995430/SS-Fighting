package com.ys.authority.controller.url.vo;

import com.ys.authority.mapper.dao.SysUrl;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "URL操作数据")
public class OperateUrlVO implements Serializable {
    @ApiModelProperty(value = "URL内容")
    SysUrl sysUrl;

    public RestResult<OperateUrlVO> success() {
        RestResult<OperateUrlVO> RestResult = new RestResult<>();
        RestResult.setData(this);
        return RestResult;
    }

    public RestResult<OperateUrlVO> error(int status, String message) {
        RestResult<OperateUrlVO> RestResult = new RestResult<>();
        RestResult.setStatus(status);
        RestResult.setMessage(message);
        RestResult.setData(this);
        return RestResult;
    }
}
