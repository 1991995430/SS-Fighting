package com.ys.authority.controller.user;

import com.ys.authority.constant.ConstDB;
import com.ys.authority.constant.ConstWeb;
import com.ys.authority.controller.login.vo.UserVO;
import com.ys.authority.controller.user.param.*;
import com.ys.authority.mapper.dao.SysUser;
import com.ys.authority.service.UserService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mxm
 * @since 2020-12-03
 */
@Slf4j
@Api(tags = "用户对象操作API")
@RestController
@RequestMapping("/authority/user")
public class UserController {
    @Resource
    private HttpServletRequest request;

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "创建用户对象")
    @PostMapping("/createUser")
    public RestResult<?> createUser(@RequestBody @Valid @ApiParam(required = true) UserAddParams addParam) {
        log.info("###[createUser]Params: {}", addParam);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        SysUser sysUser = new SysUser();
        sysUser.setName(addParam.getName());
        sysUser.setPassword(addParam.getPassword());
        sysUser.setTrueName(addParam.getTrueName());
        sysUser.setServiceId(addParam.getServiceId());
        sysUser.setSex(addParam.getSex());
        sysUser.setMobileNumber(addParam.getMobileNumber());
        sysUser.setEmail(addParam.getEmail());
        sysUser.setDeptId(addParam.getDeptId());
        sysUser.setDeptCode(addParam.getDeptCode());
        if (addParam.getValidTime() != null) {
            sysUser.setValidTime(addParam.getValidTime());
        } else {
            sysUser.setValidTime(LocalDateTime.of(2099, 12, 31, 23, 59));
        }
        return userService.createUser(sysUser, userVO, addParam);
    }

    @ApiOperation(value = "更新用户对象")
    @PostMapping("/updateUser")
    public RestResult<?> updateUser(@RequestBody @Valid @ApiParam(required = true) UserUpdateParams updateParams) {
        log.info("###[updateUser]Params: {}", updateParams);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        SysUser sysUser = updateParams.buildUserObject();
        return userService.updateUser(sysUser, userVO, updateParams);
    }

    @ApiOperation(value = "删除用户对象")
    @PostMapping("/deleteUser")
    public RestResult<?> deleteUser(@RequestBody @Valid @ApiParam(required = true) UserUpdateParams updateParams) {
        log.info("###[deleteUser]Params: {}", updateParams);
        if (updateParams.getId() == 1) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, "Super service's super user can not be deleted!");
        }
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        SysUser sysUser = new SysUser();
        sysUser.setId(updateParams.getId());

        return userService.deleteUser(sysUser, userVO);
    }

    @ApiOperation(value = "获取用户列表")
    @PostMapping("/getUserList")
    public RestResult<?> getUserList(@RequestBody @Valid @ApiParam(required = true) UserSearchParams params) {
        log.info("###[getUserList]Params: {}", params);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        SysUser sysUser = new SysUser();
        sysUser.setId(params.getId());
        sysUser.setName(params.getName());
        sysUser.setTrueName(params.getTrueName());
        sysUser.setServiceId(params.getServiceId());
        sysUser.setMobileNumber(params.getMobileNumber());
        sysUser.setSex(params.getSex());
        sysUser.setStatus(ConstDB.RECORD_VALID);  // 强制查询有效数据
        sysUser.setEmail(params.getEmail());
        return userService.getUserList(sysUser, userVO, params.getPageNum(), params.getPageSize());
    }

    @ApiOperation(value = "获取用户列表")
    @PostMapping("/getUserListByRoleName")
    @LoginAuthIgnore
    public List<SysUser> getUserListByRoleName(@RequestBody @Valid @ApiParam(required = true) UserSearchInnerReq req) {
        log.info("###[getUserListByRoleName]Params: {}", req);
        try {
            return userService.getUserListByRoleName(req);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    @ApiOperation(value = "修改用户密码")
    @PostMapping("/editPassword")
    public RestResult<?> editPassword(@RequestBody @Valid @ApiParam(required = true) UserEditPasswordParams userEditPasswordParams) {
        log.info("###[editPassword: {}", userEditPasswordParams);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        return userService.editPassword(userVO, userEditPasswordParams);
    }

    @ApiOperation(value = "管理员重置密码")
    @PostMapping("/adminResetPassword")
    public RestResult<?> adminResetPassword(@RequestBody @Valid @ApiParam(required = true) UserGetParams userGetParams) {
        log.info("###[adminResetPassWordParams {}", userGetParams);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        return userService.adminResetPassword(userGetParams);
    }
}

