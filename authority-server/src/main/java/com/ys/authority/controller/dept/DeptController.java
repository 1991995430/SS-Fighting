package com.ys.authority.controller.dept;

import com.ys.authority.constant.ConstWeb;
import com.ys.authority.controller.dept.param.DeptAddParams;
import com.ys.authority.controller.dept.param.DeptDeleteParams;
import com.ys.authority.controller.dept.param.DeptSearchParams;
import com.ys.authority.controller.dept.param.DeptUpdateParams;
import com.ys.authority.controller.login.vo.UserVO;
import com.ys.authority.service.SysDeptService;
import com.ys.authority.utils.web.CommonHeaderInfoUtil;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author spike
 */
@Slf4j
@Api(tags = "部门管理")
@RestController
@RequestMapping("/authority/dept")
public class DeptController {
    @Resource
    private HttpServletRequest request;

    private final SysDeptService sysDeptService;

    @Autowired
    public DeptController(SysDeptService sysDeptService) {
        this.sysDeptService = sysDeptService;
    }
    @ApiOperation(value = "增加部门")
    @PostMapping("/createDept")
    public RestResult<?> createDept(@RequestBody @Valid @ApiParam(required = true) DeptAddParams addParams) {
        log.info("###[createDept]Params: {}", addParams);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        return sysDeptService.createDept(addParams);
    }

    @ApiOperation(value = "更新部门")
    @PostMapping("/updateDept")
    public RestResult<?> updateDept(@RequestBody @Valid @ApiParam(required = true) DeptUpdateParams updateParams) {
        log.info("###[updateDept]Params: {}", updateParams);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        return sysDeptService.updateDept(updateParams);
    }

    @ApiOperation(value = "删除部门")
    @PostMapping("/deleteDept")
    public RestResult<?> deleteDept(@RequestBody @Valid @ApiParam(required = true) DeptDeleteParams deleteParams) {
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        return sysDeptService.deleteDept(deleteParams.getCode());
    }

    @ApiOperation(value = "获取部门以及子部门列表")
    @PostMapping("/querySubDeptList")
    public RestResult<?> querySubDeptList(@RequestBody @Valid @ApiParam(required = true) DeptSearchParams searchParams) {
        log.info("###[querySubDeptList]Params: {}", searchParams);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        return sysDeptService.querySubDeptList(searchParams);
    }

    @ApiOperation(value = "获取当前部门信息")
    @PostMapping("/queryDeptInfo")
    public RestResult<?> queryDeptInfo(@RequestBody @Valid @ApiParam(required = true) DeptSearchParams searchParams) {
        log.info("###[queryDeptList]Params: {}", searchParams);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        return sysDeptService.queryDeptInfo(searchParams);
    }
}
