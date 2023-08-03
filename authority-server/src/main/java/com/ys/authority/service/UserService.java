package com.ys.authority.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ys.authority.constant.ConstDB;
import com.ys.authority.controller.login.param.AuthParam;
import com.ys.authority.controller.login.vo.UserVO;
import com.ys.authority.controller.user.param.*;
import com.ys.authority.controller.user.vo.OperateUserVO;
import com.ys.authority.controller.user.vo.UserInfoVO;
import com.ys.authority.mapper.SysRoleMapper;
import com.ys.authority.mapper.SysServiceMapper;
import com.ys.authority.mapper.SysUserAssociateRoleMapper;
import com.ys.authority.mapper.SysUserMapper;
import com.ys.authority.mapper.dao.SysRole;
import com.ys.authority.mapper.dao.SysUser;
import com.ys.authority.mapper.dao.SysUserAssociateRole;
import com.ys.authority.utils.ServiceNameCache;
import com.ys.authority.utils.SysRoleCache;
import com.ys.common.bean.PageHelperVO;
import com.ys.common.utils.MD5;
import com.ys.common.utils.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class UserService {
    @Resource
    SysUserMapper sysUserMapper;

    @Resource
    SysRoleMapper sysRoleMapper;

    @Resource
    SysServiceMapper sysServiceMapper;

    @Resource
    SysRoleCache sysRoleCache;

    @Resource
    ServiceNameCache serviceNameCache;

    @Resource
    SysUserAssociateRoleMapper sysUserAssociateRoleMapper;

    public SysUser checkUserPassword(AuthParam authParams) {
        String userName = authParams.getUserName();
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysUser.fName, userName);
        if (authParams.getServiceId() != null) {
            queryWrapper.eq(SysUser.fServiceId, authParams.getServiceId());
        }
        queryWrapper.eq(SysUser.fStatus, ConstDB.RECORD_VALID);
        SysUser dbSysUser = sysUserMapper.selectOne(queryWrapper);
        if (dbSysUser == null) {
            log.info("Fail to check password for no user:{}", userName);
            return null;
        }
        if (dbSysUser.getValidTime() != null && LocalDateTime.now().isAfter(dbSysUser.getValidTime())) {
            log.info("Fail to check password for user invalid: {}", userName);
            return null;
        }
        String md5Password = authParams.getPassword();
        if (!md5Password.equalsIgnoreCase(dbSysUser.getPassword())) {
            log.info("Fail to check password for password error, user:{}, md5Pwd:{}, dbPwd:{}", userName, md5Password, dbSysUser.getPassword());
            return null;
        }
        return dbSysUser;
    }

    @Transactional
    public RestResult<?> createUser(SysUser sysUser, UserVO userVO, UserAddParams addParam) {
        int superAdmin = sysRoleMapper.isAdmin(userVO.getRoleIdList());
        if (superAdmin == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only admin can create user.");
        }
        if (!sysUser.getServiceId().equals(userVO.getServiceId())) {
            int superServer = sysServiceMapper.isSuperService(userVO.getServiceId());
            if (superServer == 0) {
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only super service can create other service user.");
            }
        }
        QueryWrapper<SysUser> checkUserNameWrapper = new QueryWrapper<>();
        checkUserNameWrapper.eq(SysUser.fName, sysUser.getName());
        if (sysUserMapper.selectCount(checkUserNameWrapper) > 0) {
            return RestResult.ret(RestResult.CODE_ITEM_EXISTED, "此用户名已经被使用");
        }
        sysUser.setAddUser(userVO.getId());
        sysUser.setAddTime(LocalDateTime.now());
        sysUser.setUpdateTime(LocalDateTime.now());
        sysUser.setStatus(ConstDB.RECORD_VALID);
        log.info("[createUser]role object:" + sysUser);
        int ret = sysUserMapper.insert(sysUser);
        if (ret == 0) {
            return RestResult.ret(RestResult.CODE_INNER_ERROR, "create user inner error");
        }
        List<SysUserAssociateRole> associateRoleList = insertAssociateRoleIdList(addParam, sysUser);
        OperateUserVO operateUserVO = new OperateUserVO();
        operateUserVO.setSysUser(sysUser);
        operateUserVO.setUserAssociateRoleIDList(associateRoleList);
        return RestResult.success(operateUserVO);
    }

    private List<SysUserAssociateRole> insertAssociateRoleIdList(UserAddParams addParam, SysUser sysUser) {
        List<SysUserAssociateRole> associateRoleList = new ArrayList<>();
        if (addParam.getRoleIdList() == null) {
            return associateRoleList;
        }
        for (Integer roleId : addParam.getRoleIdList()) {
            SysUserAssociateRole userAssociateRole = new SysUserAssociateRole();
            userAssociateRole.setUserId(sysUser.getId());
            userAssociateRole.setRoleId(roleId);
            userAssociateRole.setStatus(ConstDB.RECORD_VALID);
            userAssociateRole.setAddTime(LocalDateTime.now());
            userAssociateRole.setUpdateTime(LocalDateTime.now());
            sysUserAssociateRoleMapper.insert(userAssociateRole);
            associateRoleList.add(userAssociateRole);
        }
        return associateRoleList;
    }

    public RestResult<?> updateUser(SysUser sysUser, UserVO userVO, UserUpdateParams updateParams) {
        int superAdmin = sysRoleMapper.isAdmin(userVO.getRoleIdList());
        if (superAdmin == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only admin can update user.");
        }
        SysUser dbSysUser = sysUserMapper.selectById(sysUser.getId());
        if (dbSysUser == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, String.format("user %d not existed.", sysUser.getId()));
        }
        if (!dbSysUser.getServiceId().equals(userVO.getServiceId())) {
            int superServer = sysServiceMapper.isSuperService(userVO.getServiceId());
            if (superServer == 0) {
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only super service can update other service user.");
            }
        }
        fillUserFieldByInput(dbSysUser, sysUser);
        dbSysUser.setUpdateTime(LocalDateTime.now());
        log.info("[updateUser]user object:" + dbSysUser);
        int ret = sysUserMapper.updateById(dbSysUser);
        if (ret == 0) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, "update user inner error");
        }
        List<SysUserAssociateRole> resultSysUserAssociateRole = new ArrayList<>();

        if (updateParams.getRoleIdList() != null) {
            QueryWrapper<SysUserAssociateRole> sysAssociateQueryWrapper = new QueryWrapper<>();
            sysAssociateQueryWrapper.eq(SysUserAssociateRole.fUserId, sysUser.getId());
            SysUserAssociateRole sysUserAssociateRole = new SysUserAssociateRole();
            sysUserAssociateRole.setStatus(0);
            sysUserAssociateRoleMapper.update(sysUserAssociateRole, sysAssociateQueryWrapper);
            for (Integer roleIdList : updateParams.getRoleIdList()
            ) {
                QueryWrapper<SysUserAssociateRole> updateAssociateQueryWrapper = new QueryWrapper<>();
                SysUserAssociateRole updateSysUserAssociateRole = new SysUserAssociateRole();
                updateAssociateQueryWrapper.eq(SysUserAssociateRole.fUserId, sysUser.getId()).eq(SysUserAssociateRole.fRoleId, roleIdList);
                int res = sysUserAssociateRoleMapper.update(updateSysUserAssociateRole.setStatus(1).setUpdateTime(LocalDateTime.now())
                        , updateAssociateQueryWrapper);
                if (res < 1) {
                    updateSysUserAssociateRole.setUserId(sysUser.getId());
                    updateSysUserAssociateRole.setRoleId(roleIdList);
                    updateSysUserAssociateRole.setStatus(1);
                    updateSysUserAssociateRole.setAddTime(LocalDateTime.now());
                    updateSysUserAssociateRole.setUpdateTime(LocalDateTime.now());
                    sysUserAssociateRoleMapper.insert(updateSysUserAssociateRole);
                }
                resultSysUserAssociateRole.add(updateSysUserAssociateRole);
            }
        }
        OperateUserVO operateUserVO = new OperateUserVO();
        operateUserVO.setSysUser(dbSysUser);
        operateUserVO.setUserAssociateRoleIDList(resultSysUserAssociateRole);
        return RestResult.success(operateUserVO);
    }

    public RestResult<?> deleteUser(SysUser sysUser, UserVO userVO) {
        int superAdmin = sysRoleMapper.isAdmin(userVO.getRoleIdList());
        if (superAdmin == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only admin can update user.");
        }
        SysUser dbSysUser = sysUserMapper.selectById(sysUser.getId());
        if (dbSysUser == null) {
            return new RestResult<>();
        }
        if (!dbSysUser.getServiceId().equals(userVO.getServiceId())) {
            int superServer = sysServiceMapper.isSuperService(userVO.getServiceId());
            if (superServer == 0) {
                return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only super service can delete other service user.");
            }
        }
        dbSysUser.setStatus(ConstDB.RECORD_INVALID);
        dbSysUser.setUpdateTime(LocalDateTime.now());
        log.info("[deleteUser]user object:" + dbSysUser);
        int ret = sysUserMapper.updateById(dbSysUser);
        if (ret == 0) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, "delete user inner error");
        }
        QueryWrapper<SysUserAssociateRole> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.eq(SysUserAssociateRole.fUserId, sysUser.getId());
        ret = sysUserAssociateRoleMapper.update(new SysUserAssociateRole().setStatus(0), deleteWrapper);
        if (ret == 0) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, "delete user associate role inner error");
        }
        OperateUserVO operateUserVO = new OperateUserVO();
        operateUserVO.setSysUser(dbSysUser);
        return RestResult.success(operateUserVO);
    }

    public RestResult<?> getUserList(SysUser sysUser, UserVO userVO, int pageNum, int pageSize) {
        int superAdmin = sysRoleMapper.isAdmin(userVO.getRoleIdList());
        if (superAdmin == 0) {
            return RestResult.ret(RestResult.CODE_AUTH_ERR, "Only admin can query user.");
        }
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        if (sysUser.getId() != null) {
            log.info("[getUserList]add condition id: {}", sysUser.getId());
            queryWrapper.eq(SysUser.fId, sysUser.getId());
        }
        if (sysUser.getName() != null) {
            log.info("[getUserList]add condition name: {}", sysUser.getName());
            queryWrapper.like(SysUser.fName, sysUser.getName());
        }
        if (sysUser.getTrueName() != null) {
            log.info("[getUserList]add condition true name: {}", sysUser.getTrueName());
            queryWrapper.like(SysUser.fTrueName, sysUser.getTrueName());
        }
        if (sysUser.getSex() != null) {
            log.info("[getUserList]add condition sex: {}", sysUser.getSex());
            queryWrapper.eq(SysUser.fSex, sysUser.getSex());
        }
        fileServiceInfo(queryWrapper, sysUser, userVO);
        if (sysUser.getMobileNumber() != null) {
            log.info("[getUserList]add condition mobile: {}", sysUser.getMobileNumber());
            queryWrapper.like(SysUser.fMobileNumber, sysUser.getMobileNumber());
        }
        if (sysUser.getEmail() != null) {
            log.info("[getUserList]add condition email: {}", sysUser.getEmail());
            queryWrapper.like(SysUser.fEmail, sysUser.getEmail());
        }
        queryWrapper.eq(SysUser.fStatus, sysUser.getStatus());
        PageHelper.startPage(pageNum, pageSize);
        List<SysUser> sysUserList = sysUserMapper.selectList(queryWrapper);
        PageHelperVO<UserInfoVO> pageHelperVO = new PageHelperVO<>(getUserMaps(sysUserList));
        return RestResult.success(pageHelperVO);
    }

    public List<SysUser> getUserListByRoleName(UserSearchInnerReq req) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysUser.fServiceId, req.getServiceId());
        Integer roleId = getRoleIdByRoleName(req);
        if (roleId == null) {
            log.error("[getUserListByRoleName] Fail to get role id by role name: {}", req.getRoleName());
            return new ArrayList<>();
        }
        QueryWrapper<SysUserAssociateRole> aRqueryWrapper = new QueryWrapper<>();
        aRqueryWrapper.in(SysUserAssociateRole.fRoleId, roleId).eq(SysUserAssociateRole.fStatus, 1);
        List<SysUserAssociateRole> sysUserAssociateRoleList = sysUserAssociateRoleMapper.selectList((aRqueryWrapper));
        List<Integer> sysUserIdList = new ArrayList<>();
        for (SysUserAssociateRole sysUserAssociateRole : sysUserAssociateRoleList
        ) {
            sysUserIdList.add(sysUserAssociateRole.getUserId());
        }
        queryWrapper.in(SysUser.fId, sysUserIdList);
        queryWrapper.eq(SysUser.fStatus, ConstDB.RECORD_VALID);
        return sysUserMapper.selectList(queryWrapper);
    }

    private Integer getRoleIdByRoleName(UserSearchInnerReq req) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysRole.fStatus, ConstDB.RECORD_VALID);
        queryWrapper.eq(SysRole.fServiceId, req.getServiceId());
        queryWrapper.eq(SysRole.fName, req.getRoleName());
        SysRole sysRole = sysRoleMapper.selectOne(queryWrapper);
        if (sysRole == null) {
            return null;
        }
        log.info("[getRoleIdByRoleName] locate role is: {}", sysRole.getId());
        return sysRole.getId();
    }

    private void fileServiceInfo(QueryWrapper<SysUser> queryWrapper, SysUser sysUser, UserVO userVO) {
        if (sysUser.getServiceId() == null) {
            int superService = sysServiceMapper.isSuperService(userVO.getServiceId());
            if (superService == 0) {
                // 非超级服务限制为查询自己业务的数据
                log.info("[fileServiceInfo]add condition service id(1): {}", userVO.getServiceId());
                queryWrapper.eq(SysUser.fServiceId, userVO.getServiceId());
            }
            return;
        }
        if (!sysUser.getServiceId().equals(userVO.getServiceId())) {
            int superService = sysServiceMapper.isSuperService(userVO.getServiceId());
            if (superService == 0) {
                log.info("[fileServiceInfo]add condition service id(2): {}", userVO.getServiceId());
                queryWrapper.eq(SysUser.fServiceId, userVO.getServiceId());
                return;
            }
        }
        log.info("[fileServiceInfo]add condition service id(3): {}", sysUser.getServiceId());
        queryWrapper.eq(SysUser.fServiceId, sysUser.getServiceId());
    }

    private List<UserInfoVO> getUserMaps(List<SysUser> RestResultList) {
        Page<UserInfoVO> resultList = new Page<>();
        Page<SysUser> userPage = (Page<SysUser>) RestResultList;
        resultList.setTotal(userPage.getTotal());
        resultList.setPageNum(userPage.getPageNum());
        resultList.setPageSize(userPage.getPageSize());
        resultList.setPages(userPage.getPages());
        sysRoleCache.flushRoleNameMap();
        serviceNameCache.flushServiceMap();
        for (SysUser u : RestResultList) {
            QueryWrapper<SysUserAssociateRole> SysUserAssociateRoleWrapper = new QueryWrapper<>();
            SysUserAssociateRoleWrapper.eq(SysUserAssociateRole.fUserId, u.getId()).eq(SysUserAssociateRole.fStatus, 1);
            List<SysUserAssociateRole> sysUserAssociateRoleList = sysUserAssociateRoleMapper.selectList(SysUserAssociateRoleWrapper);
            List<String> roleNameList = new ArrayList<>();
            List<Integer> roleIdList = new ArrayList<>();
            for (SysUserAssociateRole sARole : sysUserAssociateRoleList
            ) {
                roleIdList.add(sARole.getRoleId());
                QueryWrapper<SysRole> SysRoleQueryWrapper = new QueryWrapper<>();
                SysRoleQueryWrapper.eq(SysRole.fId, sARole.getRoleId());
                roleNameList.add(sysRoleMapper.selectOne(SysRoleQueryWrapper).getName());
            }
            UserInfoVO user = new UserInfoVO();
            user.setId(u.getId());
            user.setName(u.getName());
            user.setTrueName(u.getTrueName());
            user.setServiceId(u.getServiceId());
            user.setSex(u.getSex());
            user.setMobileNumber(u.getMobileNumber());
            user.setEmail(u.getEmail());
            user.setValidTime(u.getValidTime());
            user.setStatus(u.getStatus());
            user.setCreateTime(u.getAddTime());
            user.setUpdateTime(u.getUpdateTime());
            user.setDeptCode(u.getDeptCode());
            user.setDeptId(u.getDeptId());
            user.setRoleNameList(roleNameList);
            user.setRoleIdList(roleIdList);
//            user.setRoleName(sysRoleCache.getRoleName(u.getRoleId()));
//            user.setAdminFlag(sysRoleCache.getRoleAdminFlag(u.getRoleId()));
            user.setServiceName(serviceNameCache.getServiceName(u.getServiceId()));
            resultList.add(user);
        }
        return resultList;
    }


    private void fillUserFieldByInput(SysUser dbSysUser, SysUser sysUser) {
        // 注意密码不在这个接口里修改
        if (sysUser.getName() != null) {
            dbSysUser.setName(sysUser.getName());
        }
        if (sysUser.getTrueName() != null) {
            dbSysUser.setTrueName(sysUser.getTrueName());
        }
//        if (sysUser.getRoleId() != null) {
//            dbSysUser.setRoleId(sysUser.getRoleId());
//        }
        if (sysUser.getSex() != null) {
            dbSysUser.setSex(sysUser.getSex());
        }
        if (sysUser.getMobileNumber() != null) {
            dbSysUser.setMobileNumber(sysUser.getMobileNumber());
        }
        if (sysUser.getEmail() != null) {
            dbSysUser.setEmail(sysUser.getEmail());
        }
        if (sysUser.getPassword() != null) {
            dbSysUser.setPassword(sysUser.getPassword());
        }
        if (sysUser.getValidTime() != null) {
            dbSysUser.setValidTime(sysUser.getValidTime());
        }
        if (sysUser.getDeptId() != null) {
            dbSysUser.setDeptId(sysUser.getDeptId());
        }
        if (sysUser.getDeptCode() != null) {
            dbSysUser.setDeptCode(sysUser.getDeptCode());
        }
    }


    public RestResult<?> editPassword(UserVO userVO, UserEditPasswordParams userEditPasswordParams) {
        boolean isCorrect = promptPasswordStrength(userEditPasswordParams.getNewPassword());
        if (!isCorrect) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, "新密码必须包含大小写字母和数字的组合，可以使用特殊字符，长度在8-16之间");
        }
        QueryWrapper<SysUser> verifyOddPassWrapper = new QueryWrapper<>();
        String oldPassWord = MD5.MD5(userEditPasswordParams.getOldPassword());
        verifyOddPassWrapper.eq(SysUser.fId, userVO.getId()).eq("password", oldPassWord);
        SysUser sysUser = sysUserMapper.selectOne(verifyOddPassWrapper);
        if (sysUser == null) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, "原密码错误");
        }
        if (!userEditPasswordParams.getNewPassword().equals(userEditPasswordParams.getConfirmNewPassword())) {
            return RestResult.ret(RestResult.CODE_PARAM_ERR, "两次密码不一致");
        }
        String newPassWord = MD5.MD5(userEditPasswordParams.getNewPassword());
        sysUser.setPassword(newPassWord);
        int res = sysUserMapper.updateById(sysUser);
        if (res == 0) {
            return RestResult.ret(RestResult.CODE_INNER_ERROR, "更新失败");
        }
        return RestResult.ret(RestResult.CODE_SUCCESS, RestResult.SUCCESS);
    }

    public RestResult<?> adminResetPassword(UserGetParams userGetParams) {
        QueryWrapper<SysUser> getUserWrapper = new QueryWrapper<>();
        getUserWrapper.eq(SysUser.fId, userGetParams.getId());
        SysUser sysUser = sysUserMapper.selectOne(getUserWrapper);
        if (sysUser == null) {
            return RestResult.ret(RestResult.CODE_CAN_NOT_FIND, "用户不存在");
        }
        sysUser.setPassword(MD5.MD5("123456"));
        int res = sysUserMapper.updateById(sysUser);
        if (res == 0) {
            return RestResult.ret(RestResult.CODE_INNER_ERROR, "更新失败");
        }
        return RestResult.ret(RestResult.CODE_SUCCESS, RestResult.SUCCESS);
    }


    public boolean promptPasswordStrength(String password) {
        String pattern = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(password);
        return m.matches();
    }


}
