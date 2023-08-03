package com.ys.authority.controller.service;

import com.ys.authority.constant.ConstDB;
import com.ys.authority.constant.ConstWeb;
import com.ys.authority.controller.login.vo.UserVO;
import com.ys.authority.controller.service.param.ServiceAddParams;
import com.ys.authority.controller.service.param.ServiceSearchParams;
import com.ys.authority.controller.service.param.ServiceUpdateParams;
import com.ys.authority.controller.service.vo.ListServiceVO;
import com.ys.authority.controller.service.vo.OperateServiceVO;
import com.ys.authority.mapper.dao.SysService;
import com.ys.authority.service.ServiceService;
import com.ys.authority.utils.annotation.LoginAuthIgnore;
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
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mxm
 * @since 2020-12-03
 */
@Slf4j
@Api(tags = "服务对象操作API")
@RestController
@RequestMapping("/authority/service")
public class ServiceController {
    @Resource
    private HttpServletRequest request;

    // =================自动装备对象================
    private final ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @ApiOperation(value = "创建服务对象")
    @PostMapping("/createService")
    public RestResult<OperateServiceVO> createService(@RequestBody @Valid @ApiParam(required = true) ServiceAddParams addParams) {
        log.info("###[createService]Params: {}", addParams);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        SysService sysService = new SysService();
        sysService.setName(addParams.getName());
        sysService.setRemark(addParams.getRemark());

        return serviceService.createService(sysService, userVO);
    }

    @ApiOperation(value = "更新服务对象")
    @PostMapping("/updateService")
    public RestResult<OperateServiceVO> updateService(@RequestBody @Valid @ApiParam(required = true) ServiceUpdateParams updateParams) {
        log.info("###[updateService]Params: {}", updateParams);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        SysService sysService = new SysService();
        sysService.setId(updateParams.getId());
        sysService.setName(updateParams.getName());
        sysService.setRemark(updateParams.getRemark());
        return serviceService.updateService(sysService, userVO);
    }

    @ApiOperation(value = "删除服务对象")
    @PostMapping("/deleteService")
    public RestResult<OperateServiceVO> deleteService(@RequestBody @Valid @ApiParam(required = true) ServiceUpdateParams updateParams) {
        log.info("###[deleteService]Params: {}", updateParams);
        if (updateParams.getId() == 1) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, "Super service can not be deleted!");
        }
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        SysService sysService = new SysService();
        sysService.setId(updateParams.getId());
        return serviceService.deleteService(sysService, userVO);
    }

    @ApiOperation(value = "获取服务列表")
    @PostMapping("/getServiceList")
    public RestResult<?> getServiceList(@RequestBody @Valid @ApiParam(required = true)
                                                ServiceSearchParams searchParams) {
        log.info("###[getServiceList]Params: {}", searchParams);
        log.info("###[getServiceList]Params: {}", request);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        SysService sysService = new SysService();
        sysService.setId(searchParams.getId());
        sysService.setName(searchParams.getName());
        sysService.setRemark(searchParams.getRemark());
        sysService.setStatus(ConstDB.RECORD_VALID);
        return serviceService.getServiceList(searchParams, userVO);
    }


    @ApiOperation(value = "不区分用户获取服务列表")
    @PostMapping("/getAllServiceList")
    @LoginAuthIgnore
    public RestResult<ListServiceVO> getAllServiceList(@RequestBody @Valid @ApiParam(required = true)
                                                   ServiceSearchParams searchParams) {
        log.info("###[getAllServiceList]Params: {}", searchParams);
        SysService sysService = new SysService();
        sysService.setStatus(ConstDB.RECORD_VALID);
        return serviceService.getServiceList(sysService, searchParams.getPageNum(), searchParams.getPageSize());
    }
}

