package com.example.yz1.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.yz1.common.Result;
import com.example.yz1.entity.Department;
import com.example.yz1.entity.Employee;
import com.example.yz1.global.GlobalUpload;
import com.example.yz1.service.IDepartmentService;
import com.example.yz1.service.IEmployeeService;
import com.example.yz1.vo.EmployeeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 员工表 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

    private final GlobalUpload globalUpload;
    private final IEmployeeService employeeService;
    private final IDepartmentService departmentService;


    /*
       修改员工角色
     */
    @PostMapping("/updateRole")
    public Result<Void> updateRole() {
        List<Department> departmentList = departmentService.lambdaQuery().list();
        List<Long> managerIds = departmentList.stream()
                .map(Department::getManagerId)
                .filter(managerId -> managerId != null)
                .distinct()
                .collect(Collectors.toList());
        boolean success = employeeService.lambdaUpdate()
                .set(Employee::getRole, 2)
                .in(Employee::getId, managerIds)
                .update();

        if (success) {
            return Result.success();
        } else {
            return Result.error("更新角色失败");
        }
    }

    /*
       新增或修改员工
     */
    @PostMapping("/addOrUpdate")
    public Result<Void> addOrUpdateEmployee(@RequestBody Employee employee) {
        boolean success = employeeService.saveOrUpdate(employee);
        if (success) {
            return Result.success();
        } else {
            return Result.error("新增失败");
        }
    }

    /*
       根据id获取员工
     */
    @GetMapping("/info/{id}")
    public Result<EmployeeVO> getEmployeeById(@PathVariable("id") Integer id) {
        Employee employee = employeeService.getById(id);
        EmployeeVO employeeVO = BeanUtil.copyProperties(employee, EmployeeVO.class);
        return Result.success(employeeVO);
    }

    /*
       根据user_id获取员工
     */
    @GetMapping("/info/user/{userId}")
    public Result<EmployeeVO> getEmployeeByUserId(@PathVariable("userId") Integer userId) {
        Employee employee = employeeService.lambdaQuery()
                .eq(Employee::getUserId, userId)
                .one();
        EmployeeVO employeeVO = BeanUtil.copyProperties(employee, EmployeeVO.class);
        return Result.success(employeeVO);
    }

    /*
       分页查询员工
     */
    @GetMapping("/page")
    public Result<IPage<Employee>> getEmployeePage(@RequestParam(defaultValue = "1") Integer pageNum,
                                                   @RequestParam(defaultValue = "5") Integer pageSize,
                                                   @RequestParam(required = false) String name,
                                                   @RequestParam(required = false) String phone) {
        Page<Employee> employeePage = employeeService.lambdaQuery()
                .like(StrUtil.isNotEmpty(name), Employee::getName, name)
                .like(StrUtil.isNotEmpty(phone), Employee::getPhone, phone)
                .page(new Page<>(pageNum, pageSize));
        return Result.success(employeePage);
    }

    /*
       根据id删除员工
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> delEmployeeById(@PathVariable("id") Integer id) {
        boolean success = employeeService.removeById(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }

    /*
        上传头像
     */
    @PostMapping("/update-avatar")
    public Result<?> updateAvatar(@RequestParam("avatar") MultipartFile file,
                                  @RequestParam("employeeId") Long employeeId) {
        try {
            if (file.isEmpty()) {
                return Result.error("头像不能为空");
            }
            String avatarUrl = globalUpload.uploadAvatar(employeeId, file, "avatar");
            employeeService.lambdaUpdate()
                    .eq(Employee::getId, employeeId)
                    .set(Employee::getAvatar, avatarUrl)
                    .update();

            Map<String, String> result = new HashMap<>();
            result.put("avatarUrl", avatarUrl);
            return Result.success(result);
        } catch (IOException e) {
            return Result.error("头像上传失败" + e.getMessage());
        }
    }

    /**
     * 上传简历
     */
    @PostMapping("/upload-resume")
    public Result<?> uploadResume(@RequestParam("resume") MultipartFile file,
                                  @RequestParam("employeeId") Long employeeId) {
        try {
            String resumeUrl = globalUpload.uploadResume(employeeId, file, "resume", new String[]{".pdf", ".doc", ".docx"});

            employeeService.lambdaUpdate()
                    .eq(Employee::getId, employeeId)
                    .set(Employee::getResumeUrl, resumeUrl)
                    .update();

            // 返回结果
            Map<String, String> result = new HashMap<>();
            result.put("resumeUrl", resumeUrl);
            return Result.success(result);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (IOException e) {
            return Result.error("简历上传失败" + e.getMessage());
        }
    }


}
