package com.ys.authority.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.ys.authority.constant.ConstDB;
import com.ys.authority.controller.login.vo.UserVO;
import com.ys.authority.controller.service.param.ServiceSearchParams;
import com.ys.authority.controller.service.vo.ListServiceVO;
import com.ys.authority.controller.service.vo.OperateServiceVO;
import com.ys.authority.mapper.SysMenuMapper;
import com.ys.authority.mapper.SysRoleMapper;
import com.ys.authority.mapper.SysServiceMapper;
import com.ys.authority.mapper.SysUrlMapper;
import com.ys.authority.mapper.dao.SysMenu;
import com.ys.authority.mapper.dao.SysRole;
import com.ys.authority.mapper.dao.SysService;
import com.ys.authority.mapper.dao.SysUrl;
import com.ys.common.bean.PageHelperVO;
import com.ys.common.utils.RestResult;
import com.ys.common.utils.StringUtil;
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
public class ServiceService {

    @Resource
    SysServiceMapper sysServiceMapper;

    @Resource
    SysRoleMapper sysRoleMapper;

    @Resource
    SysMenuMapper sysMenuMapper;

    @Resource
    SysUrlMapper sysUrlMapper;

    public RestResult<OperateServiceVO> createService(SysService sysService, UserVO userVO) {
        OperateServiceVO operateServiceVO = new OperateServiceVO();
        if (sysService.getName() == null) {
            return operateServiceVO.error(RestResult.CODE_PARAM_ERR, "miss param name.");
        }
        String errorMsg = isAuthorizedUser(userVO);
        if (errorMsg != null) {
            return operateServiceVO.error(RestResult.CODE_PARAM_ERR, errorMsg);
        }
        int sameServer = sysServiceMapper.isSameService(sysService.getName());
        if (sameServer != 0) {
            return operateServiceVO.error(RestResult.CODE_PARAM_ERR, "Server existed.");
        }
        sysService.setAddUserId(userVO.getId());
        sysService.setAddTime(LocalDateTime.now());
        sysService.setUpdateTime(LocalDateTime.now());
        sysService.setStatus(ConstDB.RECORD_VALID);

        log.info("[createServer]server object:" + sysService);
        operateServiceVO.setSysService(sysService);
        int ret = sysServiceMapper.insert(sysService);
        if (ret != 0) {
            return operateServiceVO.success();
        }
        return operateServiceVO.error(RestResult.CODE_INNER_ERROR, "save server inner error");
    }

    public RestResult<OperateServiceVO> updateService(SysService sysService, UserVO userVO) {
        String errorMsg = isAuthorizedUser(userVO);
        if (errorMsg != null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, errorMsg);
        }
        SysService dbSysService = sysServiceMapper.selectById(sysService.getId());
        if (dbSysService == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, String.format("server %d not existed.", sysService.getId()));
        }
        if (!StringUtil.isEmpty(sysService.getName()) && !sysService.getName().equals(dbSysService.getName())) {
            int sameServer = sysServiceMapper.isSameService(sysService.getName());
            if (sameServer != 0) {
                return RestResult.ret(RestResult.CODE_PARAM_ERR, "Server name is existed.");
            }
            dbSysService.setName(sysService.getName());
        }
        if (sysService.getRemark() != null) {
            dbSysService.setRemark(sysService.getRemark());
        }
        dbSysService.setUpdateTime(LocalDateTime.now());
        log.info("[updateServer]server object:" + dbSysService);
        int ret = sysServiceMapper.updateById(dbSysService);
        if (ret == 0) {
            return RestResult.ret(RestResult.CODE_INNER_ERROR, "save server inner error");
        }
        OperateServiceVO operateServiceVO = new OperateServiceVO();
        operateServiceVO.setSysService(dbSysService);
        return operateServiceVO.success();
    }

    public RestResult<OperateServiceVO> deleteService(SysService sysService, UserVO userVO) {
        OperateServiceVO operateServiceVO = new OperateServiceVO();
        String errorMsg = isAuthorizedUser(userVO);
        if (errorMsg != null) {
            return operateServiceVO.error(RestResult.CODE_PARAM_ERR, errorMsg);
        }
        SysService dbSysService = sysServiceMapper.selectById(sysService.getId());
        if (dbSysService == null) {
            return operateServiceVO.success();
        }
        if (isSomethingDependOnService(sysService.getId())) {
            return operateServiceVO.error(RestResult.CODE_PARAM_ERR, "There are role(s) in this service.");
        }
        operateServiceVO.setSysService(sysService);
        dbSysService.setStatus(ConstDB.RECORD_INVALID);
        dbSysService.setUpdateTime(LocalDateTime.now());

        log.info("[deleteServer]server object:" + dbSysService);

        int ret = sysServiceMapper.updateById(dbSysService);
        if (ret != 0) {
            return operateServiceVO.success();
        }
        return operateServiceVO.error(RestResult.CODE_INNER_ERROR, "save server inner error");
    }

    public RestResult<?> getServiceList(ServiceSearchParams searchParams, UserVO userVO) {
        QueryWrapper<SysService> wrapper = new QueryWrapper<>();
        int superService = sysServiceMapper.isSuperService(userVO.getServiceId());
        if (superService == 0) {
            wrapper.eq(SysService.fId, userVO.getServiceId());
        }
        wrapper.eq(SysService.fStatus, ConstDB.RECORD_VALID);
        PageHelper.startPage(searchParams.getPageNum(), searchParams.getPageSize());
        List<SysService> resultList = sysServiceMapper.selectList(wrapper);
        PageHelperVO pageHelperVO = new PageHelperVO(resultList);
        return RestResult.success(pageHelperVO);
    }

    public RestResult<ListServiceVO> getServiceList(SysService sysService, int pageNum, int pageSize) {
        QueryWrapper<SysService> wrapper = new QueryWrapper<>(sysService);
        PageHelper.startPage(pageNum, pageSize);
        List<SysService> resultList = sysServiceMapper.selectList(wrapper);
        ListServiceVO listServiceVO = new ListServiceVO();
        listServiceVO.setSysServiceList(resultList);
        return listServiceVO.success();
    }

    private String isAuthorizedUser(UserVO userVO) {
        // 必须是超级管理员才可以修改Server信息
        int superAdmin = sysRoleMapper.isAdmin(userVO.getRoleIdList());
        if (superAdmin == 0) {
            return "Invalid role.";
        }
        int superServer = sysServiceMapper.isSuperService(userVO.getServiceId());
        if (superServer == 0) {
            return "Invalid server level.";
        }
        return null;
    }

    private boolean isSomethingDependOnService(int serviceId) {
        QueryWrapper<SysRole> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq(SysRole.fServiceId, serviceId);
        roleQueryWrapper.eq(SysRole.fStatus, ConstDB.RECORD_VALID);
        int roleCnt = sysRoleMapper.selectCount(roleQueryWrapper);
        if (roleCnt != 0) {
            return true;
        }
        QueryWrapper<SysMenu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.eq(SysMenu.fServiceId, serviceId);
        menuQueryWrapper.eq(SysMenu.fStatus, ConstDB.RECORD_VALID);
        int menuCnt = sysMenuMapper.selectCount(menuQueryWrapper);
        if (menuCnt != 0) {
            return true;
        }
        QueryWrapper<SysUrl> urlQueryWrapper = new QueryWrapper<>();
        urlQueryWrapper.eq(SysUrl.fServiceId, serviceId);
        urlQueryWrapper.eq(SysUrl.fStatus, ConstDB.RECORD_VALID);
        int urlCnt = sysUrlMapper.selectCount(urlQueryWrapper);
        return urlCnt != 0;
    }
}
