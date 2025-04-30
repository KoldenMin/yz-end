package com.example.yz1.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.yz1.common.Result;
import com.example.yz1.dto.DepartmentUpdateDTO;
import com.example.yz1.entity.Department;
import com.example.yz1.entity.Employee;
import com.example.yz1.mapper.DepartmentMapper;
import com.example.yz1.service.IDepartmentService;
import com.example.yz1.service.IEmployeeDepartmentService;
import com.example.yz1.service.IEmployeeService;
import com.example.yz1.vo.DepartmentVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
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
    private final DepartmentMapper departmentMapper;
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
        Department department = departmentService.getById(id);
        Long oldManagerId = department.getManagerId();

        String managerName = departmentUpdateDTO.getManagerName();
        if (StrUtil.isNotEmpty(managerName)) {
            // 部门经理不为空
            Employee employee = employeeService.lambdaQuery()
                    .eq(Employee::getName, managerName)
                    .one();
            Long employeeId = employee.getId();
            Boolean success = departmentService.updateDepartment(id, departmentUpdateDTO, employeeId);
            if (success) {
                // 还要修改employee表的员工状态
                // todo 这里改部门表的管理员的时候还要重新设置employee表的role字段，不是部门经理了还要把role变成0
                // 这里设置新的部门经理的role为2
                employeeService.lambdaUpdate()
                        .eq(Employee::getId, oldManagerId)
                        .set(Employee::getRole, 0)
                        .update();
                employeeService.lambdaUpdate()
                        .eq(Employee::getId, employeeId)
                        .set(Employee::getRole, 2)  // 设置新的经理
                        .update();
                return Result.success();
            } else {
                return Result.error("更新部门失败");
            }
        } else {
            // 部门经理为空
            Boolean success = departmentService.updateDepartment(id, departmentUpdateDTO, null);
            if (success) {
                return Result.success();
            } else {
                return Result.error("更新部门失败");
            }
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
    public Result<DepartmentVO> getDepartmentInfo(@PathVariable Long id) {
        Department department = departmentService.getById(id);
        if (department == null) {
            return Result.error("查不到部门信息");
        }
        Long employeeId = department.getManagerId();
        DepartmentVO departmentVO = BeanUtil.copyProperties(department, DepartmentVO.class);
        if (employeeId == null) {
            // 部门经理为空，直接返回
            return Result.success(departmentVO);
        }
        // 不为空设置部门经理姓名
        Employee employee = employeeService.getById(employeeId);
        String employeeName = employee.getName();
        departmentVO.setManagerName(employeeName);

        return Result.success(departmentVO);
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
    public Result<IPage<DepartmentVO>> getDepartmentPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                                         @RequestParam(defaultValue = "5") Integer pageSize,
                                                         @RequestParam(required = false) String name,
                                                         @RequestParam(required = false) String depaFunction) {
        MPJLambdaWrapper<Department> wrapper = new MPJLambdaWrapper<Department>()
                .select(Department::getId, Department::getName, Department::getDepaFunction,
                        Department::getEmployeeCount, Department::getWorkingDate, Department::getWorkingHours,
                        Department::getCreateTime)
                .selectAs(Employee::getName, "managerName")
                .leftJoin(Employee.class, Employee::getId, Department::getManagerId);

        // 关闭副表逻辑查询
        wrapper.disableSubLogicDel();

        Page<DepartmentVO> page = new Page<>(pageNum, pageSize);

        wrapper.like(StrUtil.isNotEmpty(name), Department::getName, name)
                .like(StrUtil.isNotEmpty(depaFunction), Department::getDepaFunction, depaFunction);
        Page<DepartmentVO> departmentVOPage = departmentMapper.selectJoinPage(page, DepartmentVO.class, wrapper);

        return Result.success(departmentVOPage);

    }

    /**
     * 获取指定id的部门的所有成员
     */
    @GetMapping("/employeeList/{departmentId}")
    public Result<List<Employee>> getEmployeeList(@PathVariable Long departmentId) {
        List<Employee> employeeList = employeeService.lambdaQuery().eq(Employee::getDepartmentId, departmentId).list();
        return Result.success(employeeList);
    }

    /**
     * 设置部门经理
     */
    @PostMapping("/setManager")
    public Result<?> setManager() {
        return null;
    }


}
