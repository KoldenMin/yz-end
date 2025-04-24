package com.example.yz1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.yz1.entity.Department;
import com.example.yz1.entity.EmployeeDepartment;
import com.example.yz1.mapper.EmployeeDepartmentMapper;
import com.example.yz1.service.IDepartmentService;
import com.example.yz1.service.IEmployeeDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 员工部门关联表 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@Service
public class EmployeeDepartmentServiceImpl extends ServiceImpl<EmployeeDepartmentMapper, EmployeeDepartment> implements IEmployeeDepartmentService {

    @Autowired
    private IDepartmentService departmentService;

    @Override
    public List<Map<String, Object>> setEmployeeCount() {
        QueryWrapper<EmployeeDepartment> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("department_id", "COUNT(*) as employee_count")
                .groupBy("department_id");
        List<Map<String, Object>> departmentCount = listMaps(queryWrapper);
        departmentCount.forEach(department -> {
            departmentService.lambdaUpdate()
                    .eq(Department::getId, department.get("department_id"))
                    .set(Department::getEmployeeCount, department.get("employee_count"))
                    .update();
        });
        return departmentCount;
    }
}
