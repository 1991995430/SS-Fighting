package com.ys.authority.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.ys.authority.controller.login.vo.UserVO;
import com.ys.authority.controller.role.param.UrlWriteListSearchParams;
import com.ys.authority.controller.role.vo.ListUrlVO;
import com.ys.authority.mapper.SysRoleMapper;
import com.ys.authority.mapper.SysServiceMapper;
import com.ys.authority.mapper.RoleUrlWriteListMapper;
import com.ys.authority.mapper.dao.RoleUrlWhiteList;
import com.ys.authority.mapper.dao.SysRole;
import com.ys.authority.utils.SysRoleCache;
import com.ys.authority.utils.ServiceNameCache;
import com.ys.common.utils.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class UrlWhiteListService {
    @Slf4j
    private static class UrlPathContainer extends HashMap<String, UrlPathContainer> {
        private static final String pathSplitChar = "/";

        public void pushUrl(String url) {
            UrlPathContainer currentLevel = this;
            String[] fields = url.split(pathSplitChar);
            for (String field : fields) {
                if ("".equals(field)) {
                    continue;
                }
                if (!currentLevel.containsKey(field)) {
                    log.info("[pushUrl]New level: {}", field);
                    currentLevel.put(field, new UrlPathContainer());
                }
                currentLevel = currentLevel.get(field);
            }
        }

        public boolean checkUrl(String url) {
            UrlPathContainer currentLevel = this;
            String[] fields = url.split(pathSplitChar);
            for (String field : fields) {
                log.info("[checkUrl]Current field: {}", field);
                if ("".equals(field)) {
                    continue;
                }
                if (currentLevel.get("*") != null) {
                    log.info("[checkUrl]Matched field {} *", field);
                    return true;
                }
                currentLevel = currentLevel.get(field);
                if (currentLevel == null) {
                    log.info("[checkUrl]No next level at {}", field);
                    return false;
                }
            }
            return currentLevel.isEmpty() || currentLevel.containsKey("*");
        }
    }

    @Resource
    private RoleUrlWriteListMapper roleUrlWriteListMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysServiceMapper sysServiceMapper;

    @Resource
    SysRoleCache sysRoleCache;

    @Resource
    ServiceNameCache serviceNameCache;

    private UrlPathContainer urlPathContainer = new UrlPathContainer();

    public RestResult<RoleUrlWhiteList> addUrlWhiteList(RoleUrlWhiteList roleUrlWhiteList, UserVO userVO) {
        int superAdmin = sysRoleMapper.isAdmin(userVO.getRoleIdList());
        if (superAdmin == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only administrator can add url white list.");
        }
        if (!userVO.getServiceId().equals(roleUrlWhiteList.getServiceId())) {
            int superServer = sysServiceMapper.isSuperService(userVO.getServiceId());
            if (superServer == 0) {
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only super server user can update other server url.");
            }
        }
        int sameUrlWhiteList = roleUrlWriteListMapper.isSameUrlWhiteList(roleUrlWhiteList.getRoleId(),
                roleUrlWhiteList.getServiceId(), roleUrlWhiteList.getUrl());
        if (sameUrlWhiteList != 0) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, "Same url white list existed.");
        }

        int ret = roleUrlWriteListMapper.insert(roleUrlWhiteList);
        if (ret == 0) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, "add url white list inner error.");
        }
        urlWriteListInit();
        RestResult<RoleUrlWhiteList> RestResult = new RestResult<>();
        RestResult.setData(roleUrlWhiteList);
        return RestResult;
    }

    public RestResult<RoleUrlWhiteList> removeUrlWhiteList(RoleUrlWhiteList roleUrlWhiteList, UserVO userVO) {
        int superAdmin = sysRoleMapper.isAdmin(userVO.getRoleIdList());
        if (superAdmin == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only administrator can remove url white list.");
        }
        RoleUrlWhiteList dbRecord = roleUrlWriteListMapper.selectById(roleUrlWhiteList);
        if (dbRecord == null) {
            return new RestResult<>();
        }
        if (!userVO.getServiceId().equals(dbRecord.getServiceId())) {
            int superServer = sysServiceMapper.isSuperService(userVO.getServiceId());
            if (superServer == 0) {
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only super server user can remove other server url.");
            }
        }
        roleUrlWriteListMapper.deleteById(dbRecord);
        urlWriteListInit();
        RestResult<RoleUrlWhiteList> RestResult = new RestResult<>();
        RestResult.setData(dbRecord);
        return RestResult;
    }

    public RestResult<ListUrlVO> getUrlWhiteList(UrlWriteListSearchParams req, UserVO userVO) {
        log.debug("getUrlList entry, req: {}", req);
        QueryWrapper<RoleUrlWhiteList> queryWrapper = new QueryWrapper<>();
        if (userVO.getRoleIdList() != null) {
            queryWrapper.in(RoleUrlWhiteList.fRoleId, userVO.getRoleIdList());
        }
        if (req.getServiceId() == null || !req.getServiceId().equals(userVO.getServiceId())) {
            int superService = sysServiceMapper.isSuperService(userVO.getServiceId());
            if (superService == 0) {
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only super server user can query other server info.");
            }
        }
        if (req.getServiceId() != null) {
            queryWrapper.eq(RoleUrlWhiteList.fServiceId, req.getServiceId());
        }
        sysRoleCache.flushRoleNameMap();
        serviceNameCache.flushServiceMap();
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<RoleUrlWhiteList> roleUrlWhiteList = roleUrlWriteListMapper.selectList(queryWrapper);
        List<Map<String, Object>> res = new ArrayList<>();
        for (RoleUrlWhiteList url : roleUrlWhiteList) {
            log.info("UrlWhiteList: {}", url);
            Map<String, Object> resMap = new LinkedHashMap<>();
            resMap.put(RoleUrlWhiteList.fId, url.getId());
            resMap.put(RoleUrlWhiteList.fRoleId, url.getRoleId());
            resMap.put(RoleUrlWhiteList.fServiceId, url.getServiceId());
            resMap.put(RoleUrlWhiteList.fUrl, url.getUrl());
            resMap.put("roleName", sysRoleCache.getRoleName(url.getRoleId()));
            resMap.put("serviceName", serviceNameCache.getServiceName(url.getServiceId()));
            res.add(resMap);
        }
        ListUrlVO listUrlVO = new ListUrlVO();
        listUrlVO.setUrlList(res);
        return listUrlVO.success();
    }

    public boolean checkUrlWriteList(String url, List<SysRole> sysRoleList) {
        if (urlPathContainer.size() == 0) {
            urlWriteListInit();
        }
        for (SysRole sysRole : sysRoleList) {
            UrlPathContainer rolePathContainer = urlPathContainer.get(sysRole.getId().toString());
            if (rolePathContainer != null) {
                if (rolePathContainer.checkUrl(url)) {
                    return true;
                }
            }
        }
        log.error("role id {} can not get write list.", sysRoleList);
        return false;
    }

    public void urlWriteListInit() {
        log.info("urlWriteListInit entry.");
        urlPathContainer.clear();
        QueryWrapper<RoleUrlWhiteList> queryWrapper = new QueryWrapper<>();
        List<RoleUrlWhiteList> urlList = roleUrlWriteListMapper.selectList(queryWrapper);
        for (RoleUrlWhiteList white : urlList) {
            String roleId = String.valueOf(white.getRoleId());
            if (!urlPathContainer.containsKey(roleId)) {
                urlPathContainer.put(roleId, new UrlPathContainer());
            }
            UrlPathContainer rolePathContainer = urlPathContainer.get(roleId);
            log.info("[urlWriteListInit]Construct role {} path: {}", roleId, white.getUrl());
            rolePathContainer.pushUrl(white.getUrl());
        }
    }
}
