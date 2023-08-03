package com.ys.authority.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.authority.constant.ConstDB;
import com.ys.authority.controller.login.vo.UserVO;
import com.ys.authority.controller.url.param.UrlQueryParams;
import com.ys.authority.controller.url.vo.OperateUrlVO;
import com.ys.authority.mapper.SysRoleMapper;
import com.ys.authority.mapper.SysServiceMapper;
import com.ys.authority.mapper.SysUrlMapper;
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
public class SysUrlService {
    @Resource
    SysUrlMapper sysUrlMapper;

    @Resource
    SysRoleMapper sysRoleMapper;

    @Resource
    SysServiceMapper sysServiceMapper;

    public RestResult<OperateUrlVO> createUrl(SysUrl sysUrl, UserVO userVO) {
        int superRole = sysRoleMapper.isAdmin(userVO.getRoleIdList());
        if (superRole == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only admin can create url.");
        }
        if (!sysUrl.getServiceId().equals(userVO.getServiceId())) {
            int superService = sysServiceMapper.isSuperService(userVO.getServiceId());
            if (superService == 0) {
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only super service can create other's url.");
            }
        }
        int sameUrl = sysUrlMapper.isSameUrl(sysUrl.getServiceId(), sysUrl.getUrlItem());
        if (sameUrl != 0) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, "Have a same url in system.");
        }
        sysUrl.setAddUserId(userVO.getId());
        sysUrl.setServiceId(sysUrl.getServiceId());
        sysUrl.setAddTime(LocalDateTime.now());
        sysUrl.setUpdateTime(LocalDateTime.now());
        sysUrl.setStatus(ConstDB.RECORD_VALID);
        log.info("[createUrl]url object:" + sysUrl);
        int ret = sysUrlMapper.insert(sysUrl);
        if (ret == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "save url inner error");
        }
        OperateUrlVO operateUrlVO = new OperateUrlVO();
        operateUrlVO.setSysUrl(sysUrl);
        return operateUrlVO.success();
    }

    public RestResult<OperateUrlVO> updateUrl(SysUrl sysUrl, UserVO userVO) {
        int superRole = sysRoleMapper.isAdmin(userVO.getRoleIdList());
        if (superRole == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only admin can update url.");
        }
        SysUrl dbSysUrl = sysUrlMapper.selectById(sysUrl.getId());
        if (dbSysUrl == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, String.format("url %d not existed.", sysUrl.getId()));
        }
        int superService = sysServiceMapper.isSuperService(userVO.getServiceId());
        if (superService == 0) {
            if (!dbSysUrl.getServiceId().equals(userVO.getServiceId())) { // 只能操作对应server id的内容
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "Can not update other service's url.");
            }
        }
        dbSysUrl.setUrlItem(sysUrl.getUrlItem());
        dbSysUrl.setUpdateTime(LocalDateTime.now());
        int ret = sysUrlMapper.updateById(dbSysUrl);
        if (ret == 0) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, "update url inner error");
        }
        OperateUrlVO operateUrlVO = new OperateUrlVO();
        operateUrlVO.setSysUrl(dbSysUrl);
        return operateUrlVO.success();
    }

    public RestResult<OperateUrlVO> deleteUrl(SysUrl sysUrl, UserVO userVO) {
        int superRole = sysRoleMapper.isAdmin(userVO.getRoleIdList());
        if (superRole == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only admin can delete url.");
        }
        SysUrl dbSysUrl = sysUrlMapper.selectById(sysUrl.getId());
        if (dbSysUrl == null) {
            return new RestResult<>();
        }
        int superService = sysServiceMapper.isSuperService(userVO.getServiceId());
        if (superService == 0) {
            if (!dbSysUrl.getServiceId().equals(userVO.getServiceId())) {
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "Can not delete other service url.");
            }
        }
        dbSysUrl.setStatus(ConstDB.RECORD_INVALID);
        dbSysUrl.setUpdateTime(LocalDateTime.now());
        log.info("[deleteUrl]url object:" + dbSysUrl);
        int ret = sysUrlMapper.updateById(dbSysUrl);
        if (ret == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "delete url inner error");
        }
        OperateUrlVO operateUrlVO = new OperateUrlVO();
        operateUrlVO.setSysUrl(dbSysUrl);
        return operateUrlVO.success();
    }

    public RestResult<?> getSysUrlList(UrlQueryParams params, UserVO userVO) {
        QueryWrapper<SysUrl> wrapper = new QueryWrapper<>();
        int superService = sysServiceMapper.isSuperService(userVO.getServiceId());
        if (superService == 0) {    // 不是超级服务，则只能看自己服务的url
            if (params.getServiceId() != null && !params.getServiceId().equals(userVO.getServiceId())) {
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "No auth to get other service's url");
            } else {
                log.info("[getSysUrlList]Add condition serviceId: {}", userVO.getServiceId());
                wrapper.eq(SysUrl.fServiceId, userVO.getServiceId());
            }
        } else {
            if (params.getServiceId() != null) {
                log.info("[getSysUrlList]Add condition serviceId: {}", params.getServiceId());
                wrapper.eq(SysUrl.fServiceId, params.getServiceId());
            }
        }
        wrapper.eq(SysUrl.fStatus, ConstDB.RECORD_VALID);
        List<SysUrl> RestResultList = sysUrlMapper.selectList(wrapper);
        PageHelperVO pageHelperVO = new PageHelperVO(RestResultList);
        return RestResult.success(pageHelperVO);
    }
}
