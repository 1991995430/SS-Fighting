package com.ys.authority.controller.dept.vo;

import com.ys.authority.mapper.dao.SysDept;
import com.ys.common.bean.PageHelperVO;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author spike
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "dept查询结果")
public class ListDeptVO extends PageHelperVO<SysDept> {
@ApiModelProperty(value = "部门列表")
List<SysDept> sysDeptList;

    public RestResult<ListDeptVO> success() {
        buildPageInfo(sysDeptList);
        RestResult<ListDeptVO> result = new RestResult<>();
        result.setData(this);
        return result;
    }
}
