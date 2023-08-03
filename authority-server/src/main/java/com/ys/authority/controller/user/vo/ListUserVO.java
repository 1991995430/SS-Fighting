package com.ys.authority.controller.user.vo;

import com.ys.authority.mapper.dao.SysRole;
import com.ys.common.bean.PageHelperVO;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "用户查询结果")
public class ListUserVO extends PageHelperVO<UserInfoVO> {

    @ApiModelProperty(value = "用户列表")
    List<UserInfoVO> userList;

    public RestResult<ListUserVO> success() {
        buildPageInfo(userList);
        RestResult<ListUserVO> result = new RestResult<>();
        result.setData(this);
        return result;
    }
}
