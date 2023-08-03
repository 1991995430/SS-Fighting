package com.ys.authority.controller.role.vo;

import com.ys.common.bean.PageHelperVO;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "访问白名单查询结果")
public class ListUrlVO extends PageHelperVO<Map<String, Object>> {
    @ApiModelProperty(value = "白名单列表")
    List<Map<String, Object>> urlList;

    public RestResult<ListUrlVO> success() {
        buildPageInfo(urlList);
        RestResult<ListUrlVO> result = new RestResult<>();
        result.setData(this);
        return result;
    }
}
