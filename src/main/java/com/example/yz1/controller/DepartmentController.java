package com.example.yz1.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.yz1.common.Result;
import com.example.yz1.dto.DepartmentUpdateDTO;
import com.example.yz1.entity.Department;
import com.example.yz1.entity.Employee;
import com.example.yz1.service.IDepartmentService;
import com.example.yz1.service.IEmployeeDepartmentService;
import com.example.yz1.service.IEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final IDepartmentService departmentService;
    private final IEmployeeService employeeService;
    private final IEmployeeDepartmentService employeeDepartmentService;

    /**
     * 添加部门
     */
    @PostMapping("/add")
    public Result<Void> addDepartment(@RequestBody Department department) {
        boolean success = departmentService.save(department);
        if (success) {
            return Result.success();
        } else {
            return Result.error("新增部门失败");
        }
    }

    /**
     * 根据id修改部门
     */
    @PutMapping("/update/{id}")
    public Result<Void> updateDepartment(@PathVariable Long id, @RequestBody DepartmentUpdateDTO departmentUpdateDTO) {
        Boolean success = departmentService.updateDepartment(id, departmentUpdateDTO);
        if (success) {
            return Result.success();
        } else {
            return Result.error("更新部门失败");
        }
    }

    /**
     * 修改或更新部门
     */
    @PostMapping("/update")
    public Result<Void> updateDepartment(@RequestBody Department department) {
        boolean success = departmentService.saveOrUpdate(department);
        if (success) {
            return Result.success();
        } else {
            return Result.error("修改或更新部门失败");
        }
    }

    /**
     * 根据id查看部门信息
     */
    @GetMapping("/info/{id}")
    public Result<Department> getDepartmentInfo(@PathVariable Long id) {
        Department department = departmentService.getById(id);
        if (department == null) {
            return Result.error("查不到部门信息");
        }
        return Result.success(department);
    }

    /**
     * 查询所有部门信息
     */
    @GetMapping("/list")
    public Result<List<Department>> getAllDepartment() {
        List<Department> list = departmentService.list();
        return Result.success(list);
    }

    /**
     * 根据id删部门
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteDepartment(@PathVariable Long id) {
        boolean success = departmentService.removeById(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除部门失败");
        }
    }

    /**
     * 分页
     */
    @GetMapping("/page")
    public Result<IPage<Department>> getDepartmentPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                                       @RequestParam(defaultValue = "5") Integer pageSize,
                                                       @RequestParam(required = false) String name,
                                                       @RequestParam(required = false) String depaFunction) {
        Page<Department> page = new Page<>(pageNum, pageSize);
        Page<Department> departmentPage = departmentService.lambdaQuery()
                .like(StrUtil.isNotEmpty(name), Department::getName, name)
                .like(StrUtil.isNotEmpty(depaFunction), Department::getDepaFunction, depaFunction).page(page);
        return Result.success(departmentPage);

    }

    /**
     * 获取指定id的部门的所有成员
     */
    @GetMapping("/employeeList/{departmentId}")
    public Result<List<Employee>> getEmployeeList(@PathVariable Long departmentId) {
        List<Employee> employeeList = employeeService.lambdaQuery().eq(Employee::getDepartmentId, departmentId).list();
        return Result.success(employeeList);
    }


}
