package com.ys.authority.controller.service.vo;

import com.ys.authority.mapper.dao.SysService;
import com.ys.common.bean.PageHelperVO;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "服务查询结果")
public class ListServiceVO extends PageHelperVO<SysService> {
    @ApiModelProperty(value = "业务列表")
    List<SysService> sysServiceList;

    public RestResult<ListServiceVO> success() {
        buildPageInfo(sysServiceList);
        RestResult<ListServiceVO> result = new RestResult<>();
        result.setData(this);
        return result;
    }
}
