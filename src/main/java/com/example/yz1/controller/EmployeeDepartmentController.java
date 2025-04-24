package com.example.yz1.controller;


import com.example.yz1.service.IDepartmentService;
import com.example.yz1.service.IEmployeeDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 员工部门关联表 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/employee-department")
public class EmployeeDepartmentController {

    private final IEmployeeDepartmentService employeeDepartmentService;
    private final IDepartmentService departmentService;


//    /**
//     * 更新部门人数统计
//     */
//    @GetMapping("/employeeCount")
//    public Result<List<Map<String, Object>>> setEmployeeCount() {
//        List<Map<String, Object>> departmentCount = employeeDepartmentService.setEmployeeCount();
//        return Result.success(departmentCount);
//    }

}
