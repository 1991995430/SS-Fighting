package com.ys.authority.controller.url;

import com.ys.authority.constant.ConstWeb;
import com.ys.authority.controller.login.vo.UserVO;
import com.ys.authority.controller.url.param.UrlAddParams;
import com.ys.authority.controller.url.param.UrlDeleteParams;
import com.ys.authority.controller.url.param.UrlQueryParams;
import com.ys.authority.controller.url.param.UrlUpdateParams;
import com.ys.authority.controller.url.vo.OperateUrlVO;
import com.ys.authority.mapper.dao.SysUrl;
import com.ys.authority.service.SysUrlService;
import com.ys.authority.utils.web.CommonHeaderInfoUtil;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Api(tags = "权限URL列表API")
@RestController
@RequestMapping("/authority/url")
public class UrlController {
    @Resource
    private HttpServletRequest request;

    @Resource
    SysUrlService sysUrlService;

    @ApiOperation(value = "创建URL项")
    @PostMapping("/createUrl")
    public RestResult<OperateUrlVO> createUrl(@RequestBody @Valid @ApiParam(required = true) UrlAddParams addParams) {
        log.info("###[createUrl]Params: {}", addParams);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        SysUrl sysUrl = addParams.buildUrlObject();
        return sysUrlService.createUrl(sysUrl, userVO);
    }

    @ApiOperation(value = "更新URL项")
    @PostMapping("/updateUrl")
    public RestResult<OperateUrlVO> updateUrl(@RequestBody @Valid @ApiParam(required = true) UrlUpdateParams updateParams) {
        log.info("###[updateUrl]Params: {}", updateParams);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        SysUrl sysUrl = updateParams.buildUrlObject();
        return sysUrlService.updateUrl(sysUrl, userVO);
    }

    @ApiOperation(value = "删除URL项")
    @PostMapping("/deleteUrl")
    public RestResult<OperateUrlVO> deleteUrl(@RequestBody @Valid @ApiParam(required = true) UrlDeleteParams params) {
        log.info("###[deleteUrl]Params: {}", params);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        SysUrl sysUrl = new SysUrl();
        sysUrl.setId(params.getId());
        return sysUrlService.deleteUrl(sysUrl, userVO);
    }

    @ApiOperation(value = "获取URL项，不分页")
    @PostMapping("/getSysUrlList")
    public RestResult<?> getSysUrlList(@RequestBody @Valid @ApiParam UrlQueryParams params) {
        log.info("###[getSysUrlList]Params: {}", params);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        return sysUrlService.getSysUrlList(params, userVO);
    }
}
