package com.ys.authority.controller.menu;

import com.ys.authority.constant.ConstWeb;
import com.ys.authority.controller.login.vo.UserVO;
import com.ys.authority.controller.menu.param.MenuAddParams;
import com.ys.authority.controller.menu.param.MenuDeleteParams;
import com.ys.authority.controller.menu.param.MenuQueryParams;
import com.ys.authority.controller.menu.param.MenuUpdateParams;
import com.ys.authority.controller.menu.vo.OperateMenuVO;
import com.ys.authority.mapper.dao.SysMenu;
import com.ys.authority.service.SysMenuService;
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
@Api(tags = "权限菜单列表API")
@RestController
@RequestMapping("/authority/menu")
public class MenuController {
    @Resource
    SysMenuService sysMenuService;

    @Resource
    private HttpServletRequest request;

    @ApiOperation(value = "创建Menu项")
    @PostMapping("/createMenu")
    public RestResult<OperateMenuVO> createMenu(@RequestBody @Valid @ApiParam(required = true) MenuAddParams params) {
        log.info("###[createMenu]Params: {}", params);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        SysMenu sysMenu = params.buildMenuObject();
        return sysMenuService.createMenu(sysMenu, userVO);
    }

    @ApiOperation(value = "更新Menu项")
    @PostMapping("/updateMenu")
    public RestResult<OperateMenuVO> updateMenu(@RequestBody @Valid @ApiParam(required = true) MenuUpdateParams params) {
        log.info("###[updateMenu]Params: {}", params);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        SysMenu sysMenu = params.buildMenuObject();
        return sysMenuService.updateMenu(sysMenu, userVO);
    }

    @ApiOperation(value = "删除Menu项")
    @PostMapping("/deleteMenu")
    public RestResult<OperateMenuVO> deleteMenu(@RequestBody @Valid @ApiParam(required = true) MenuDeleteParams params) {
        log.info("###[deleteMenu]Params: {}", params);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        SysMenu sysMenu = new SysMenu();
        sysMenu.setId(params.getId());
        return sysMenuService.deleteMenu(sysMenu, userVO);
    }

    @ApiOperation(value = "获取菜单项，不分页")
    @PostMapping("/getSysMenuList")
    public RestResult<?> getSysMenuList(@RequestBody @Valid @ApiParam MenuQueryParams params) {
        log.info("###[getSysMenuList]Params: {}", params);
        UserVO userVO = CommonHeaderInfoUtil.getUserInfoFromHttpRequest(request);
        if (userVO == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, ConstWeb.ERR_STR_GET_USER);
        }
        if (params == null) {
            params = new MenuQueryParams();
        }
        return sysMenuService.getSysMenuList(params, userVO);
    }
}
