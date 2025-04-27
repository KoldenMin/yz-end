package com.example.yz1.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.yz1.common.Result;
import com.example.yz1.entity.Department;
import com.example.yz1.entity.Employee;
import com.example.yz1.entity.RegistrationApproval;
import com.example.yz1.mapper.RegistrationApprovalMapper;
import com.example.yz1.service.IDepartmentService;
import com.example.yz1.service.IRegistrationApprovalService;
import com.example.yz1.vo.RegistrationApprovalVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 注册审核表 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/registration-approval")
public class RegistrationApprovalController {

    private final IRegistrationApprovalService registrationApprovalService;
    private final IDepartmentService departmentService;
    private final RegistrationApprovalMapper registrationApprovalMapper;

    /*
       新增或修改注册同意表记录
     */
    @PostMapping("/addOrUpdate")
    public Result<Void> addOrUpdateEmployee(@RequestBody RegistrationApproval registrationApproval) {
        boolean success = registrationApprovalService.saveOrUpdate(registrationApproval);

        // 如果有部门id并且状态审核通过，执行监听，
//        if (registrationApproval.getTargetDepartmentId() != null && registrationApproval.getStatus() == 1) {
//            try {
//                EmployeeEvent event = new EmployeeEvent(this);
//                applicationEventPublisher.publishEvent(event);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }

        if (success) {
            return Result.success();
        } else {
            return Result.error("新增失败");
        }
    }

    /*
       分页查询需审核用户
     */
    @GetMapping("/page")
    public Result<IPage<RegistrationApprovalVO>> getERegisterApprovalPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                                                          @RequestParam(defaultValue = "5") Integer pageSize,
                                                                          @RequestParam(required = false) String realName,
                                                                          @RequestParam(required = false) Integer status) {

        MPJLambdaWrapper<RegistrationApproval> wrapper = new MPJLambdaWrapper<RegistrationApproval>()
                .select(RegistrationApproval::getId, RegistrationApproval::getUserId, RegistrationApproval::getRealName,
                        RegistrationApproval::getStatus,
                        RegistrationApproval::getApprovalOpinion, RegistrationApproval::getApprovalTime)
                .selectAs(Department::getName, "departmentName")
                .leftJoin(Department.class, Department::getId, RegistrationApproval::getTargetDepartmentId)
                .selectAs(Employee::getName, "employeeName")
                .leftJoin(Employee.class, Employee::getId, RegistrationApproval::getApproverId);

        // 关闭副表逻辑删除
        wrapper.disableSubLogicDel();

        Page<RegistrationApprovalVO> page = new Page<>(pageNum, pageSize);
        if (StrUtil.isNotEmpty(realName)) {
            wrapper.like(RegistrationApproval::getRealName, realName);
        }
        if (status != null) {
            wrapper.eq(RegistrationApproval::getStatus, status);
        }

        Page<RegistrationApprovalVO> resultPage = registrationApprovalMapper.selectJoinPage(page, RegistrationApprovalVO.class, wrapper);

        return Result.success(resultPage);
    }

    /*
       删除已经审核通过的用户
     */
    @DeleteMapping("/delete")
    public Result<Boolean> deleteRegisterApproval() {
        LambdaQueryWrapper<RegistrationApproval> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RegistrationApproval::getStatus, 1);
        return Result.success(registrationApprovalService.remove(queryWrapper));
    }

    /*
       获取单个审核记录详情
     */
    @GetMapping("/{id}")
    public Result<RegistrationApproval> getById(@PathVariable Long id) {
        return Result.success(registrationApprovalService.getById(id));
    }

    /*
       批量审核
     */
    @PostMapping("/batchApprove")
    public Result<Boolean> batchApprove(@RequestBody List<RegistrationApproval> registrationApprovals) {
        return Result.success(registrationApprovalService.updateBatchById(registrationApprovals));
    }

    /*
       统计功能
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 待审核数量
        Long pendingCount = registrationApprovalService.lambdaQuery()
                .eq(RegistrationApproval::getStatus, 0)
                .count();

        // 通过数量
        long approvedCount = registrationApprovalService.lambdaQuery()
                .eq(RegistrationApproval::getStatus, 1)
                .count();

        // 拒绝数量
        long rejectedCount = registrationApprovalService.lambdaQuery()
                .eq(RegistrationApproval::getStatus, 2)
                .count();

        statistics.put("待审核数量", pendingCount);
        statistics.put("审核通过数量", approvedCount);
        statistics.put("审核拒绝数量", rejectedCount);

        return Result.success(statistics);
    }

    @GetMapping("/getAllDepartment")
    public Result<List<Department>> getAllDepartment() {
        List<Department> list = departmentService.list();
        return Result.success(list);
    }

}
