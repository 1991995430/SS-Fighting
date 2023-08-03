package com.ys.authority.controller.role.vo;

import com.ys.authority.mapper.dao.RoleMenuWhiteList;
import com.ys.common.bean.PageHelperVO;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "访问白名单查询结果")
public class ListMenuVO extends PageHelperVO<RoleMenuWhiteList> {
    @ApiModelProperty(value = "白名单列表")
    List<RoleMenuWhiteList> data;
    @ApiModelProperty(value = "白名单总列表")
    String totalMenu;

    public RestResult<ListMenuVO> success() {
        buildPageInfo(data);
        RestResult<ListMenuVO> result = new RestResult<>();
        result.setData(this);
        return result;
    }
}
