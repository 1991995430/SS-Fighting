package com.ys.authority.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.ys.authority.controller.dept.param.DeptAddParams;
import com.ys.authority.controller.dept.param.DeptSearchParams;
import com.ys.authority.controller.dept.param.DeptUpdateParams;
import com.ys.authority.controller.dept.vo.OperateDeptVO;
import com.ys.authority.mapper.SysDeptMapper;
import com.ys.authority.mapper.SysUserMapper;
import com.ys.authority.mapper.dao.SysDept;
import com.ys.authority.mapper.dao.SysUser;
import com.ys.common.bean.PageHelperVO;
import com.ys.common.utils.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author spike
 */
@Slf4j
@Service
public class SysDeptService {
    @Resource
    SysDeptMapper sysDeptMapper;
    @Resource
    SysUserMapper sysUserMapper;

    public RestResult<?> querySubDeptList(DeptSearchParams searchParams) {
        QueryWrapper<SysDept> isDeptWrapper = new QueryWrapper<>();
        isDeptWrapper.eq(SysDept.fDeptCode, searchParams.getCode());
        SysDept sysDept = sysDeptMapper.selectOne(isDeptWrapper);
        if (sysDept == null) {
            return RestResult.ret(RestResult.CODE_INNER_ERROR, "此部门不存在");
        }
        //从当前部门开始，查询指定深度的部门信息
        QueryWrapper<SysDept> wrapper = new QueryWrapper<>();
        wrapper.likeRight(SysDept.fDeptCode, sysDept.getCode()).
                gt(SysDept.fDepth, sysDept.getDepth()).le(SysDept.fDepth, sysDept.getDepth() + searchParams.getSearchDepth());
        List<SysDept> resultList = sysDeptMapper.selectList(wrapper);
        return RestResult.success(resultList);
    }

    public RestResult<?> queryDeptInfo(DeptSearchParams searchParams) {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        if (searchParams.getCode() != null && !"".equals(searchParams.getCode().trim())) {
            queryWrapper.like(SysDept.fDeptCode, searchParams.getCode());
        }
        if (searchParams.getId() != null) {
            queryWrapper.eq(SysDept.fDeptId, searchParams.getId());
        }
        if (searchParams.getName() != null && !"".equals(searchParams.getName().trim())) {
            queryWrapper.like(SysDept.fDeptName, searchParams.getName());
        }
        queryWrapper.orderByAsc(SysDept.fDeptCode);
        PageHelper.startPage(searchParams.getPageNum(), searchParams.getPageSize());
        List<SysDept> result = sysDeptMapper.selectList(queryWrapper);
        PageHelperVO pageHelperVO = new PageHelperVO(result);
        return RestResult.success(pageHelperVO);
    }

    public RestResult<?> createDept(DeptAddParams addParams) {
        QueryWrapper<SysDept> isDeptWrapper = new QueryWrapper<>();
        isDeptWrapper.eq(SysDept.fDeptCode, addParams.getCode());
        SysDept dept = sysDeptMapper.selectOne(isDeptWrapper);
        if (dept == null) {
            return RestResult.ret(RestResult.CODE_INNER_ERROR, "没有该部门信息，请刷新页面");
        }
        QueryWrapper<SysDept> wrapper = new QueryWrapper<>();
        wrapper.eq(SysDept.fDepth, dept.getDepth() + 1).likeRight(SysDept.fDeptCode, dept.getCode()).orderByAsc(SysDept.fDeptCode);
        List<SysDept> result = sysDeptMapper.selectList(wrapper);
        SysDept sysDept = generateDeptInfo(result, dept.getCode(), addParams);
        int res = sysDeptMapper.insert(sysDept);
        if (res == 0) {
            return RestResult.ret(RestResult.CODE_INNER_ERROR, "创建部门失败");
        }
        OperateDeptVO operateDeptVO = new OperateDeptVO();
        operateDeptVO.setSysDept(sysDept);
        return RestResult.success(operateDeptVO);
    }

    public RestResult<?> updateDept(DeptUpdateParams updateParams) {
        SysDept sysDept= sysDeptMapper.selectById(updateParams.getId());
        if (sysDept == null ) {
            return RestResult.ret(RestResult.CODE_INNER_ERROR, "没有该部门信息，无法更新");
        }
        sysDept.setName(updateParams.getName());
        sysDept.setDescription(updateParams.getDescription());
        int res = sysDeptMapper.updateById(sysDept);
        if (res == 0) {
            return RestResult.ret(RestResult.CODE_INNER_ERROR, "更新失败");
        }
        return RestResult.ret(RestResult.CODE_SUCCESS, RestResult.SUCCESS);
    }

    public RestResult<?> deleteDept(String code) {
        QueryWrapper<SysUser> isContainsNextWrapper = new QueryWrapper<>();
        isContainsNextWrapper.likeRight(SysUser.fDeptCode, code);
        if (sysUserMapper.selectCount(isContainsNextWrapper) > 0) {
            return RestResult.ret(RestResult.CODE_INNER_ERROR, "该部门或下级部门已关联用户，无法删除");
        }
        QueryWrapper<SysDept> wrapper = new QueryWrapper<>();
        wrapper.likeRight(SysDept.fDeptCode, code);
        sysDeptMapper.delete(wrapper);
        return RestResult.ret(RestResult.CODE_SUCCESS, "删除成功");
    }

    private SysDept generateDeptInfo(List<SysDept> result, String deptCode, DeptAddParams addParams) {
        SysDept sysDept = new SysDept();
        if (result.size() == 0) {
            sysDept.setCode(deptCode + "001");
        } else {
            int i;
            for (i = 0; i < result.size(); i++) {
                String code = result.get(i).getCode();
                int numberCode = Integer.parseInt(code.substring(code.length() - 3));
                if (numberCode != (i + 1)) {
                    break;
                }
            }
            sysDept.setCode(deptCode + String.format("%03d", (i + 1)));
        }
        sysDept.setAddTime(LocalDateTime.now());
        sysDept.setName(addParams.getName());
        sysDept.setDepth(addParams.getDepth() + 1);
        sysDept.setDescription(addParams.getDescription());
        return sysDept;
    }
}
