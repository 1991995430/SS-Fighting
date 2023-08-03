package com.ys.authority.controller.role;

import com.ys.authority.constant.ConstWeb;
import com.ys.authority.controller.login.vo.UserVO;
import com.ys.authority.controller.role.param.RoleAddParams;
import com.ys.authority.controller.role.param.RoleSearchParams;
import com.ys.authority.controller.role.param.RoleUpdateParams;
import com.ys.authority.controller.role.vo.OperateRoleVO;
import com.ys.authority.mapper.dao.SysRole;
import com.ys.authority.service.RoleService;
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
 *  前端控制器
 * </p>
 *
 * @author mxm
 * @since 2020-12-03
 */
@Api(tags = "角色对象操作API")
@RestController
@RequestMapping("/authority/role")
@Slf4j
public class RoleController {
    @Resource
    private HttpServletRequest request;

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @ApiOperation(value = "创建角色对象")
    @PostMapping("/createRole")
    public RestResult<OperateRoleVO> createRole(@RequestBody @Valid @ApiParam(required = true) RoleAddParams addParams) {
        log.info("###[createRole]Params: {}", addParams);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        SysRole sysRole = addParams.buildRoleObject();
        return roleService.createRole(sysRole, userVO);
    }

    @ApiOperation(value = "更新角色对象")
    @PostMapping("/updateRole")
    public RestResult<OperateRoleVO> updateRole(@RequestBody @Valid @ApiParam(required = true) RoleUpdateParams updateParams) {
        log.info("###[updateRole]Params: {}", updateParams);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        SysRole sysRole = updateParams.buildRoleObject();
        return roleService.updateRole(sysRole, userVO);
    }

    @ApiOperation(value = "删除角色对象")
    @PostMapping("/deleteRole")
    public RestResult<OperateRoleVO> deleteRole(@RequestBody @Valid @ApiParam(required = true) RoleUpdateParams updateParams) {
        log.info("###[deleteRole]Params: {}", updateParams);
        if (updateParams.getId() == 1) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, "Super service's super role can not be deleted!");
        }
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        SysRole sysRole = new SysRole();
        sysRole.setId(updateParams.getId());  // 这里只要一个id，使用build可能会被其他字段干扰
        return roleService.deleteRole(sysRole, userVO);
    }

    @ApiOperation(value = "获取角色列表")
    @PostMapping("/getRoleList")
    public RestResult<?> getRoleList(@RequestBody @Valid @ApiParam(required = true) RoleSearchParams searchParams) {
        log.info("###[getRoleList]Params: {}", searchParams);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        SysRole sysRole = searchParams.buildRoleObj();
        return roleService.getRoleList(sysRole, userVO, searchParams.getPageNum(), searchParams.getPageSize());
    }
}


