package com.ys.authority.controller.role;

import com.ys.authority.constant.ConstWeb;
import com.ys.authority.controller.login.vo.UserVO;
import com.ys.authority.controller.role.param.UrlWhiteListAddParams;
import com.ys.authority.controller.role.param.UrlWhiteListDeleteParams;
import com.ys.authority.controller.role.param.UrlWriteListSearchParams;
import com.ys.authority.controller.role.vo.ListUrlVO;
import com.ys.authority.mapper.dao.RoleUrlWhiteList;
import com.ys.authority.service.UrlWhiteListService;
import com.ys.authority.utils.web.CommonHeaderInfoUtil;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Api(tags = "服务接口白名单API")
@RestController
@RequestMapping("/authority/urlWriteList")
public class UrlWriteListController {
    @Resource
    private HttpServletRequest request;

    private final UrlWhiteListService urlWhiteListService;

    @Autowired
    public UrlWriteListController(UrlWhiteListService urlWhiteListService) {
        this.urlWhiteListService = urlWhiteListService;
    }

    @ApiOperation(value = "添加url白名单")
    @PostMapping("/add")
    public RestResult<RoleUrlWhiteList> add(@RequestBody UrlWhiteListAddParams addParams) {
        log.info("###[add]Params:{}", addParams);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        RoleUrlWhiteList roleUrlWhiteList = new RoleUrlWhiteList();
        roleUrlWhiteList.setRoleId(addParams.getRoleId());
        roleUrlWhiteList.setServiceId(addParams.getServiceId());
        roleUrlWhiteList.setUrl(addParams.getUrl());
        return urlWhiteListService.addUrlWhiteList(roleUrlWhiteList, userVO);
    }

    @ApiOperation(value = "移除url白名单")
    @PostMapping("/remove")
    public RestResult<RoleUrlWhiteList> remove(@RequestBody UrlWhiteListDeleteParams deleteParams) {
        log.info("###[remove]Params:{}", deleteParams);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        RoleUrlWhiteList roleUrlWhiteList = new RoleUrlWhiteList();
        roleUrlWhiteList.setId(deleteParams.getId());
        return urlWhiteListService.removeUrlWhiteList(roleUrlWhiteList, userVO);
    }

    @ApiOperation(value = "获取url白名单列表")
    @PostMapping("/getUrlWhiteList")
    public RestResult<ListUrlVO> getUrlWhiteList(@RequestBody UrlWriteListSearchParams req) {
        log.info("###[getUrlWhiteList]Params:{}", req);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        return urlWhiteListService.getUrlWhiteList(req, userVO);
    }
}
