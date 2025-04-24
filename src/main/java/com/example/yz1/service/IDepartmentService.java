package com.example.yz1.service;

import com.example.yz1.dto.DepartmentUpdateDTO;
import com.example.yz1.entity.Department;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
public interface IDepartmentService extends IService<Department> {

    Boolean updateDepartment(Long id, DepartmentUpdateDTO departmentUpdateDTO);

    void updateDepartmentEmployeeCount(Long departmentId);
}
