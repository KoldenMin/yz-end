package com.example.yz1.service;

import com.example.yz1.entity.EmployeeDepartment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 员工部门关联表 服务类
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
public interface IEmployeeDepartmentService extends IService<EmployeeDepartment> {

    List<Map<String, Object>> setEmployeeCount();
}
