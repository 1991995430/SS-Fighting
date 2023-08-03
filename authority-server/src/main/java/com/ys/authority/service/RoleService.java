package com.ys.authority.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.ys.authority.constant.ConstDB;
import com.ys.authority.controller.login.vo.UserVO;
import com.ys.authority.controller.role.vo.OperateRoleVO;
import com.ys.authority.mapper.*;
import com.ys.authority.mapper.dao.*;
import com.ys.common.bean.PageHelperVO;
import com.ys.common.utils.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mxm
 * @since 2020-12-03
 */
@Slf4j
@Service
public class RoleService {

    @Resource
    SysRoleMapper sysRoleMapper;

    @Resource
    SysServiceMapper sysServiceMapper;

    @Resource
    SysUserMapper sysUserMapper;

    @Resource
    RoleMenuWhiteListMapper roleMenuWhiteListMapper;

    @Resource
    RoleUrlWriteListMapper roleUrlWriteListMapper;

    @Resource
    SysUserAssociateRoleMapper sysUserAssociateRoleMapper;

    public RestResult<OperateRoleVO> createRole(SysRole sysRole, UserVO userVO) {
        int superAdmin = sysRoleMapper.isAdmin(userVO.getRoleIdList());
        if (superAdmin == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only administrator can create role.");
        }
        if (!userVO.getServiceId().equals(sysRole.getServiceId())) {
            int superServer = sysServiceMapper.isSuperService(userVO.getServiceId());
            if (superServer == 0) {
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only super server user can create other server role.");
            }
        }
        int sameRole = sysRoleMapper.isSameRole(sysRole.getServiceId(), sysRole.getName());
        if (sameRole != 0) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, "Role is existed.");
        }
        sysRole.setAddUserId(userVO.getId());
        sysRole.setAddTime(LocalDateTime.now());
        sysRole.setUpdateTime(LocalDateTime.now());
        sysRole.setStatus(ConstDB.RECORD_VALID);
        log.info("[createRole]role object:" + sysRole);
        int ret = sysRoleMapper.insert(sysRole);
        if (ret == 0) {
            return RestResult.ret(RestResult.CODE_INNER_ERROR, "save role inner error");
        }
        OperateRoleVO operateRoleVO = new OperateRoleVO();
        operateRoleVO.setSysRole(sysRole);
        return operateRoleVO.success();
    }

    public RestResult<OperateRoleVO> updateRole(SysRole sysRole, UserVO userVO) {
        int superAdmin = sysRoleMapper.isAdmin(userVO.getRoleIdList());
        if (superAdmin == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only administrator can update role.");
        }
        SysRole dbSysRole = sysRoleMapper.selectById(sysRole.getId());
        if (dbSysRole == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, String.format("role %d not existed.", sysRole.getId()));
        }
        if (!userVO.getServiceId().equals(dbSysRole.getServiceId())) {
            int superServer = sysServiceMapper.isSuperService(userVO.getServiceId());
            if (superServer == 0) {
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only super server user can update other server role.");
            }
        }
        if (sysRole.getName() != null && !sysRole.getName().isEmpty() && !dbSysRole.getName().equals(sysRole.getName())) {
            int sameRole = sysRoleMapper.isSameRole(dbSysRole.getServiceId(), sysRole.getName());
            if (sameRole != 0) {
                return RestResult.ret(RestResult.CODE_INNER_ERROR, "Role name is existed.");
            }
        }
        fillUpdateField(dbSysRole, sysRole);
        int ret = sysRoleMapper.updateById(dbSysRole);
        if (ret == 0) {
            return RestResult.ret(RestResult.CODE_INNER_ERROR, "update role inner error");
        }
        OperateRoleVO operateRoleVO = new OperateRoleVO();
        operateRoleVO.setSysRole(dbSysRole);
        return operateRoleVO.success();
    }

    public RestResult<OperateRoleVO> deleteRole(SysRole sysRole, UserVO userVO) {
        int superAdmin = sysRoleMapper.isAdmin(userVO.getRoleIdList());
        if (superAdmin == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only administrator can delete role.");
        }
        SysRole dbSysRole = sysRoleMapper.selectById(sysRole.getId());
        if (dbSysRole == null) {
            return new RestResult<>();
        }
        if (!userVO.getServiceId().equals(dbSysRole.getServiceId())) {
            int superServer = sysServiceMapper.isSuperService(userVO.getServiceId());
            if (superServer == 0) {
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only super server user can delete other server role.");
            }
        }
        if (isUserDependOnRole(sysRole.getId())) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, "There are user(s) or white list belong to the role.");
        }
        dbSysRole.setStatus(ConstDB.RECORD_INVALID);
        dbSysRole.setUpdateTime(LocalDateTime.now());
        OperateRoleVO operateRoleVO = new OperateRoleVO();
        operateRoleVO.setSysRole(dbSysRole);
        log.info("[deleteRole]role object: {}", dbSysRole);
        int ret = sysRoleMapper.updateById(dbSysRole);
        if (ret == 0) {
            return RestResult.ret(RestResult.CODE_INNER_ERROR, "delete role inner error");
        }
        return operateRoleVO.success();
    }

    public RestResult<?> getRoleList(SysRole sysRole, UserVO userVO, int pageNum, int pageSize) {
        int superServer = sysServiceMapper.isSuperService(userVO.getServiceId());
        if (superServer == 0) { // 超级服务者可以不用serverId进行搜索
            sysRole.setServiceId(userVO.getServiceId());
        }
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>(sysRole);
        PageHelper.startPage(pageNum, pageSize);
        List<SysRole> resultList = sysRoleMapper.selectList(wrapper);
        PageHelperVO pageHelperVO = new PageHelperVO(resultList);
        return RestResult.success(pageHelperVO);
    }

    private void fillUpdateField(SysRole dbSysRole, SysRole input) {
        if (input.getName() != null && !input.getName().isEmpty()) {
            dbSysRole.setName(input.getName());
        }
        if (input.getRemark() != null) {
            dbSysRole.setRemark(input.getRemark());
        }
        dbSysRole.setStatus(input.getStatus());
        dbSysRole.setUpdateTime(LocalDateTime.now());
        log.info("[updateRole]role object:" + dbSysRole);
    }

    private boolean isUserDependOnRole(int roleId) {
        QueryWrapper<SysUserAssociateRole> sysUserAssociateRoleQueryWrapper = new QueryWrapper<>();
        sysUserAssociateRoleQueryWrapper.eq(SysUserAssociateRole.fRoleId, roleId).eq(SysUserAssociateRole.fStatus, 1);
        int userCnt = sysUserAssociateRoleMapper.selectCount(sysUserAssociateRoleQueryWrapper);
        if (userCnt != 0) {
            return true;
        }
        QueryWrapper<RoleMenuWhiteList> menuWhiteListQueryWrapper = new QueryWrapper<>();
        menuWhiteListQueryWrapper.eq(RoleMenuWhiteList.fRoleId, roleId);
        int menuCnt = roleMenuWhiteListMapper.selectCount(menuWhiteListQueryWrapper);
        if (menuCnt != 0) {
            return true;
        }
        QueryWrapper<RoleUrlWhiteList> urlWhiteListQueryWrapper = new QueryWrapper<>();
        urlWhiteListQueryWrapper.eq(RoleUrlWhiteList.fRoleId, roleId);
        int urlCnt = roleUrlWriteListMapper.selectCount(urlWhiteListQueryWrapper);
        return urlCnt != 0;
    }
}
