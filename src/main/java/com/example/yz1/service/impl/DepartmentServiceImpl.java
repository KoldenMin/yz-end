package com.example.yz1.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yz1.dto.DepartmentUpdateDTO;
import com.example.yz1.entity.Department;
import com.example.yz1.mapper.DepartmentMapper;
import com.example.yz1.mapper.EmployeeDepartmentMapper;
import com.example.yz1.mapper.EmployeeMapper;
import com.example.yz1.service.IDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@RequiredArgsConstructor
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    private final DepartmentMapper departmentMapper;
    private final EmployeeMapper employeeMapper;
    private final EmployeeDepartmentMapper employeeDepartmentMapper;

    @Override
    public Boolean updateDepartment(Long id, DepartmentUpdateDTO departmentUpdateDTO, Long employeeId) {
        Department selectDepartment = getById(id);
        if (selectDepartment == null) {
            return false;
        }
        if (StrUtil.isNotEmpty(departmentUpdateDTO.getName())) {
            selectDepartment.setName(departmentUpdateDTO.getName());
        }
        if (StrUtil.isNotEmpty(departmentUpdateDTO.getDepaFunction())) {
            selectDepartment.setDepaFunction(departmentUpdateDTO.getDepaFunction());
        }
        if (StrUtil.isNotEmpty(departmentUpdateDTO.getWorkingDate())) {
            selectDepartment.setWorkingDate(departmentUpdateDTO.getWorkingDate());
        }
        if (StrUtil.isNotEmpty(departmentUpdateDTO.getWorkingHours())) {
            selectDepartment.setWorkingHours(departmentUpdateDTO.getWorkingHours());
        }
        if (StrUtil.isNotEmpty(departmentUpdateDTO.getManagerName())) {
            selectDepartment.setManagerId(employeeId);
        }
        return updateById(selectDepartment);
    }

    @Override
    public void updateDepartmentEmployeeCount(Long departmentId) {
//        if (departmentId == null) {
//            return;
//        }
//        // 方法1：通过员工主部门统计
//        int primaryCount = employeeMapper.countByDepartmentId(departmentId);
//
//        // 方法2：通过关联表统计（如果考虑一个员工可能同时属于多个部门）
//        int relationCount = employeeDepartmentMapper.countByDepartmentId(departmentId);
//
//        // 根据业务需求决定使用哪种计数方式
//        // 这里我们假设使用主部门计数方式
//        int finalCount = primaryCount;
//        // 更新部门表的人数字段
//        Department department = new Department();
//        department.setId(departmentId);
//        department.setEmployeeCount(finalCount);
//        departmentMapper.updateById(department);
    }

    /**
     * 批量更新所有部门的人数统计
     * 可用于系统维护或数据修复
     */
    public void updateAllDepartmentsEmployeeCount() {
        List<Department> departments = list();
        for (Department department : departments) {
            updateDepartmentEmployeeCount(department.getId());
        }
    }


}
