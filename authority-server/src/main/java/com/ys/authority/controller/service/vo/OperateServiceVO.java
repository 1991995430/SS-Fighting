package com.ys.authority.controller.service.vo;

import com.ys.authority.mapper.dao.SysService;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "服务对象操作数据")
public class OperateServiceVO implements Serializable {
    @ApiModelProperty(value = "服务内容")
    SysService sysService;

    public RestResult<OperateServiceVO> success() {
        RestResult<OperateServiceVO> result = new RestResult<>();
        result.setData(this);
        return result;
    }

    public RestResult<OperateServiceVO> error(int status, String message) {
        RestResult<OperateServiceVO> result = new RestResult<>();
        result.setStatus(status);
        result.setMessage(message);
        result.setData(this);
        return result;
    }
}
