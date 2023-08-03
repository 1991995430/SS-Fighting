package com.ys.authority.controller.role;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.authority.constant.ConstWeb;
import com.ys.authority.controller.login.vo.UserVO;
import com.ys.authority.controller.role.param.MenuWhiteListSearchParams;
import com.ys.authority.controller.role.param.MenuWhiteListUpdateParams;
import com.ys.authority.controller.role.vo.ListMenuVO;
import com.ys.authority.mapper.RoleMenuWhiteListMapper;
import com.ys.authority.mapper.SysRoleMapper;
import com.ys.authority.mapper.SysServiceMapper;
import com.ys.authority.mapper.dao.RoleMenuWhiteList;
import com.ys.authority.service.SysMenuService;
import com.ys.authority.utils.web.CommonHeaderInfoUtil;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Api(tags = "菜单目录白名单API")
@RestController
@RequestMapping("/authority/menuWriteList")
public class MenuWhiteListController {
    @Resource
    RoleMenuWhiteListMapper roleMenuWhiteListMapper;

    @Resource
    SysRoleMapper sysRoleMapper;

    @Resource
    SysServiceMapper sysServiceMapper;

    @Resource
    private final HttpServletRequest request;

    @Resource
    SysMenuService sysMenuService;

    public MenuWhiteListController(HttpServletRequest request) {
        this.request = request;
    }

    @ApiOperation(value = "添加url白名单")
    @PostMapping("/update")
    public RestResult<RoleMenuWhiteList> update(@RequestBody MenuWhiteListUpdateParams updateParams) {
        log.info("###[update]Params: {}", updateParams);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        RoleMenuWhiteList roleMenuWhiteList = new RoleMenuWhiteList();
        roleMenuWhiteList.setRoleId(updateParams.getRoleId());
        roleMenuWhiteList.setServiceId(updateParams.getServiceId());
        roleMenuWhiteList.setMenu(updateParams.getMenu());

        return updateRoleMenu(roleMenuWhiteList, userVO);
    }

    @ApiOperation(value = "查询角色的menu白名单")
    @PostMapping("/getRoleMenuWhiteList")
    public RestResult<ListMenuVO> getRoleMenuWhiteList(@RequestBody MenuWhiteListSearchParams params, HttpServletRequest request) {
        log.info("###[getRoleMenuWhiteList]params: {}", params);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        QueryWrapper<RoleMenuWhiteList> queryWrapper = new QueryWrapper<>();
        if (params.getServiceId() == null || !params.getServiceId().equals(userVO.getServiceId())) {
            int superService = sysServiceMapper.isSuperService(userVO.getServiceId());
            if (superService == 0) {    // 非超级服务，只能查询自己服务的url
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only super server user can query other server info.");
            }
        }
        if (params.getRoleId() != null) {
            queryWrapper.eq(RoleMenuWhiteList.fRoleId, params.getRoleId());
        }
        if (params.getServiceId() != null) {
            queryWrapper.eq(RoleMenuWhiteList.fServiceId, params.getServiceId());
        }
        List<RoleMenuWhiteList> whiteLists = roleMenuWhiteListMapper.selectList(queryWrapper);
        ListMenuVO listMenuVO = new ListMenuVO();
        listMenuVO.setData(whiteLists);
        return listMenuVO.success();
    }

    private RestResult<RoleMenuWhiteList> updateRoleMenu(RoleMenuWhiteList roleMenuWhiteList, UserVO userVO) {
        int superAdmin = sysRoleMapper.isAdmin(userVO.getRoleIdList());
        if (superAdmin == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only administrator can update rule menu.");
        }
        if (!userVO.getServiceId().equals(roleMenuWhiteList.getServiceId())) {
            int superServer = sysServiceMapper.isSuperService(userVO.getServiceId());
            if (superServer == 0) {
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only super server user can update other server menu.");
            }
        }
        QueryWrapper<RoleMenuWhiteList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(RoleMenuWhiteList.fRoleId, roleMenuWhiteList.getRoleId());
        queryWrapper.eq(RoleMenuWhiteList.fServiceId, roleMenuWhiteList.getServiceId());
        RoleMenuWhiteList dbRecord = roleMenuWhiteListMapper.selectOne(queryWrapper);
        int ret;
        RestResult<RoleMenuWhiteList> result = new RestResult<>();
        if (dbRecord == null) {
            ret = roleMenuWhiteListMapper.insert(roleMenuWhiteList);
            result.setData(roleMenuWhiteList);
        } else {
            dbRecord.setMenu(roleMenuWhiteList.getMenu());
            ret = roleMenuWhiteListMapper.updateById(dbRecord);
            result.setData(dbRecord);
        }
        if (ret == 0) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, "Update menu inner error.");
        }
        return result;
    }

    @ApiOperation(value = "查询用户的menu白名单")
    @PostMapping("/getUserMenuWhiteList")
    public RestResult<ListMenuVO> getUserMenuWhiteList(@RequestBody MenuWhiteListSearchParams params) {
        log.info("###[getRoleMenuWhiteList]params: {}", params);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        if (params.getRoleIdList().isEmpty()) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, "该用户没有绑定角色");
        }
        QueryWrapper<RoleMenuWhiteList> queryWrapper = new QueryWrapper<>();
        if (params.getRoleIdList() != null) {
            queryWrapper.in(RoleMenuWhiteList.fRoleId, params.getRoleIdList());
        }
        if (params.getServiceId() == null || !params.getServiceId().equals(userVO.getServiceId())) {
            int superService = sysServiceMapper.isSuperService(userVO.getServiceId());
            if (superService == 0) {    // 非超级服务，只能查询自己服务的url
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only super server user can query other server info.");
            }
        }
        if (params.getServiceId() != null) {
            queryWrapper.eq(RoleMenuWhiteList.fServiceId, params.getServiceId());
        }
        List<RoleMenuWhiteList> whiteLists = roleMenuWhiteListMapper.selectList(queryWrapper);
        ListMenuVO listMenuVO = new ListMenuVO();
        listMenuVO.setData(whiteLists);
        String totalMenu = "";
        if (!whiteLists.isEmpty()) {
            totalMenu = sysMenuService.contactMenu(whiteLists);
        }
        listMenuVO.setTotalMenu(totalMenu);
        return listMenuVO.success();
    }
}
