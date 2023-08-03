package com.ys.authority.entity.vo;

import com.ys.common.utils.RestResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "对象操作结果")
public class OperateVO implements Serializable {
    @ApiModelProperty(value = "修改对象数量")
    int count;

    public RestResult<OperateVO> success() {
        RestResult<OperateVO> result = new RestResult<>();
        result.setData(this);
        return result;
    }

    public RestResult<OperateVO> error(int status, String message) {
        RestResult<OperateVO> result = new RestResult<>();
        result.setStatus(status);
        result.setMessage(message);
        result.setData(this);
        return result;
    }
}
