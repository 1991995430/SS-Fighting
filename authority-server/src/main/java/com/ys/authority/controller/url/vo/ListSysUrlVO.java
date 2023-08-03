package com.ys.authority.controller.url.vo;

import com.ys.authority.mapper.dao.SysUrl;
import com.ys.common.bean.PageHelperVO;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "URL询结果")
public class ListSysUrlVO extends PageHelperVO<SysUrl> {
    @ApiModelProperty(value = "菜单列表")
    List<SysUrl> sysUrlList;

    public RestResult<ListSysUrlVO> success() {
        buildPageInfo(sysUrlList);
        RestResult<ListSysUrlVO> result = new RestResult<>();
        result.setData(this);
        return result;
    }
}
