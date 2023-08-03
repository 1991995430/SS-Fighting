package com.ys.authority.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ys.authority.constant.ConstDB;
import com.ys.authority.controller.login.vo.UserVO;
import com.ys.authority.controller.menu.param.MenuQueryParams;
import com.ys.authority.controller.menu.vo.OperateMenuVO;
import com.ys.authority.entity.pojo.JsonInfo;
import com.ys.authority.mapper.SysMenuMapper;
import com.ys.authority.mapper.SysRoleMapper;
import com.ys.authority.mapper.SysServiceMapper;
import com.ys.authority.mapper.dao.RoleMenuWhiteList;
import com.ys.authority.mapper.dao.SysMenu;
import com.ys.authority.mapper.dao.SysUrl;
import com.ys.common.bean.PageHelperVO;
import com.ys.common.utils.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class SysMenuService {
    @Resource
    SysMenuMapper sysMenuMapper;

    @Resource
    SysRoleMapper sysRoleMapper;

    @Resource
    SysServiceMapper sysServiceMapper;
    private static Gson gson = new Gson();

    public RestResult<OperateMenuVO> createMenu(SysMenu sysMenu, UserVO userVO) {
        int superRole = sysRoleMapper.isAdmin(userVO.getRoleIdList());
        if (superRole == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only admin can create menu.");
        }
        if (!sysMenu.getServiceId().equals(userVO.getServiceId())) {
            int superService = sysServiceMapper.isSuperService(userVO.getServiceId());
            if (superService == 0) {
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only super service can create other's menu.");
            }
        }
        int sameUrl = sysMenuMapper.isSameMenu(sysMenu.getServiceId(), sysMenu.getMenuItem());
        if (sameUrl != 0) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, "Have a same menu in system.");
        }
        sysMenu.setAddUserId(userVO.getId());
        sysMenu.setAddTime(LocalDateTime.now());
        sysMenu.setUpdateTime(LocalDateTime.now());
        sysMenu.setStatus(ConstDB.RECORD_VALID);
        log.info("[createMenu]menu object:" + sysMenu);
        int ret = sysMenuMapper.insert(sysMenu);
        if (ret == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "save menu inner error");
        }
        OperateMenuVO operateMenuVO = new OperateMenuVO();
        operateMenuVO.setData(sysMenu);
        return operateMenuVO.success();
    }

    public RestResult<OperateMenuVO> updateMenu(SysMenu sysMenu, UserVO userVO) {
        int superRole = sysRoleMapper.isAdmin(userVO.getRoleIdList());
        if (superRole == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only admin can update menu.");
        }
        SysMenu dbSysMenu = sysMenuMapper.selectById(sysMenu.getId());
        if (dbSysMenu == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, String.format("menu %d not existed.", sysMenu.getId()));
        }
        int superService = sysServiceMapper.isSuperService(userVO.getServiceId());
        if (superService == 0) {
            if (!dbSysMenu.getServiceId().equals(userVO.getServiceId())) { // 只能操作对应server id的内容
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "Can not update other service's menu.");
            }
        }
        dbSysMenu.setMenuItem(sysMenu.getMenuItem());
        dbSysMenu.setUpdateTime(LocalDateTime.now());
        int ret = sysMenuMapper.updateById(dbSysMenu);
        if (ret == 0) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, "update menu inner error");
        }
        OperateMenuVO operateMenuVO = new OperateMenuVO();
        operateMenuVO.setData(dbSysMenu);
        return operateMenuVO.success();
    }

    public RestResult<OperateMenuVO> deleteMenu(SysMenu sysMen, UserVO userVO) {
        int superRole = sysRoleMapper.isAdmin(userVO.getRoleIdList());
        if (superRole == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only admin can delete menu.");
        }
        SysMenu dbSysMenu = sysMenuMapper.selectById(sysMen.getId());
        if (dbSysMenu == null) {
            return new RestResult<>();
        }
        int superService = sysServiceMapper.isSuperService(userVO.getServiceId());
        if (superService == 0) {
            if (!dbSysMenu.getServiceId().equals(userVO.getServiceId())) {
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "Can not delete other service menu.");
            }
        }
        dbSysMenu.setStatus(ConstDB.RECORD_INVALID);
        dbSysMenu.setUpdateTime(LocalDateTime.now());
        log.info("[deleteMenu]menu object:" + dbSysMenu);
        int ret = sysMenuMapper.updateById(dbSysMenu);
        if (ret == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "delete menu inner error");
        }
        OperateMenuVO operateMenuVO = new OperateMenuVO();
        operateMenuVO.setData(dbSysMenu);
        return operateMenuVO.success();
    }

    public RestResult<?> getSysMenuList(MenuQueryParams params, UserVO userVO) {
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        int superService = sysServiceMapper.isSuperService(userVO.getServiceId());
        if (superService == 0) {    // 不是超级服务，则只能看自己服务的menu
            if (params.getServiceId() != null && !params.getServiceId().equals(userVO.getServiceId())) {
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "no auth to get other service's menu");
            } else {
                wrapper.eq(SysMenu.fServiceId, userVO.getServiceId());
            }
        } else {
            if (params.getServiceId() != null) {
                wrapper.eq(SysMenu.fServiceId, params.getServiceId());
            }
        }
        wrapper.eq(SysUrl.fStatus, ConstDB.RECORD_VALID);
        List<SysMenu> RestResultList = sysMenuMapper.selectList(wrapper);
        PageHelperVO pageHelperVO = new PageHelperVO(RestResultList);
        return RestResult.success(pageHelperVO);
    }

    public String contactMenu(List<RoleMenuWhiteList> whiteLists) {
        List<JsonInfo> leftNode = new Gson().fromJson(whiteLists.get(0).getMenu(), new TypeToken<List<JsonInfo>>() {
        }.getType());
        int length = whiteLists.size();
        if(length == 1){
            return gson.toJson(leftNode);
        }
        for (int i = 1; i < length; i++) {
            List<JsonInfo> rightNode = new Gson().fromJson(whiteLists.get(i).getMenu(), new TypeToken<List<JsonInfo>>() {
            }.getType());
            merge(leftNode.get(0), rightNode.get(0));
        }
        return gson.toJson(leftNode);
    }

    public static void merge(JsonInfo leftNode, JsonInfo rightNode) {
        if (rightNode.getChildren().isEmpty()) {
            return;
        }
        for (JsonInfo child : rightNode.getChildren()
        ) {
            JsonInfo sameNode = getNodeByID(leftNode.getChildren(), child.getId());
            if (sameNode == null) {
                leftNode.children.add(child);
            } else {
                merge(sameNode, child);
            }
        }

    }

    private static JsonInfo getNodeByID(List<JsonInfo> nodeList, String id) {
        if (nodeList.isEmpty()) {
            return null;
        }
        for (JsonInfo jsonInfo : nodeList) {
            if (jsonInfo.id.equals(id)) {
                return jsonInfo;
            }
        }
        return null;
    }
}
