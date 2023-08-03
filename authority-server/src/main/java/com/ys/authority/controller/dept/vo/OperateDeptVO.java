package com.ys.authority.controller.dept.vo;

import com.ys.authority.mapper.dao.SysDept;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author spike
 */
@Data
@ApiModel(value = "部门操作数据")
public class OperateDeptVO {
    @ApiModelProperty(value = "部门内容")
    SysDept sysDept;
    public RestResult<OperateDeptVO> success() {
        RestResult<OperateDeptVO> result = new RestResult<>();
        result.setData(this);
        return result;
    }

    public RestResult<OperateDeptVO> error(int status, String message) {
        RestResult<OperateDeptVO> result = new RestResult<>();
        result.setStatus(status);
        result.setMessage(message);
        result.setData(this);
        return result;
    }
}
