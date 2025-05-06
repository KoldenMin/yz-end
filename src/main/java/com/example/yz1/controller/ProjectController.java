package com.example.yz1.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.yz1.common.Result;
import com.example.yz1.entity.Employee;
import com.example.yz1.entity.Project;
import com.example.yz1.entity.ProjectParticipant;
import com.example.yz1.mapper.ProjectParticipantMapper;
import com.example.yz1.service.IEmployeeService;
import com.example.yz1.service.IProjectParticipantService;
import com.example.yz1.service.IProjectService;
import com.example.yz1.vo.ProjectVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 项目表 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/project")
public class ProjectController {
    private final IProjectService projectService;
    private final IProjectParticipantService projectParticipantService;
    private final IEmployeeService employeeService;
    private final ProjectParticipantMapper projectParticipantMapper;

    // 新增、修改项目
    @PostMapping("/addOrUpdate")
    public Result<Void> addOrUpdate(@RequestBody Project project) {
        boolean success = projectService.saveOrUpdate(project);
        if (success) {
            return Result.success();
        } else {
            return Result.error("新增项目失败");
        }
    }

    // 根据id查看项目信息
    @GetMapping("/info/{id}")
    public Result<Project> info(@PathVariable("id") Long id) {
        return Result.success(projectService.getById(id));
    }

    // 查看所有项目信息
    @GetMapping("/list")
    public Result<List<Project>> listAll() {
        List<Project> list = projectService.list();
        return Result.success(list);
    }

    // 根据id删除项目
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteProjectByID(@PathVariable("id") Long id) {
        boolean success = projectService.removeById(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除项目失败");
        }
    }

    // 分页
    @GetMapping("/page")
    public Result<IPage<Project>> getProjectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                                 @RequestParam(defaultValue = "5") Integer pageSize,
                                                 @RequestParam(required = false) String name,
                                                 @RequestParam(required = false) String description) {
        Page<Project> page = new Page<>(pageNum, pageSize);
        Page<Project> projectPage = projectService.lambdaQuery()
                .like(StrUtil.isNotEmpty(name), Project::getName, name)
                .like(StrUtil.isNotEmpty(description), Project::getDescription, description)
                .page(page);
        return Result.success(projectPage);
    }

    // 根据项目id获取所有参与该项目的员工
    @GetMapping("/userList/{projectId}")
    public Result<ProjectVO> getProjectUserList(@PathVariable("projectId") Long projectId) {
        // 根据项目id查询参与信息
        List<ProjectParticipant> projectParticipantList = projectParticipantService.lambdaQuery()
                .eq(projectId != null, ProjectParticipant::getProjectId, projectId)
                .list();
        if (ObjectUtil.isEmpty(projectParticipantList)) {
            return Result.error("该项目目前没有参与信息");
        }
        //获取参与信息中所有员工id
        List<Long> empIds = projectParticipantList.stream()
                .map(ProjectParticipant::getEmployeeId)
                .distinct()
                .collect(Collectors.toList());
        //获取员工列表
        List<Employee> employeeList = employeeService.lambdaQuery()
                .in(Employee::getId, empIds)
                .list();
        if (ObjectUtil.isEmpty(employeeList)) {
            return Result.error("该项目参与的员工列表为空");
        }
        // 获取项目信息封装projectvo
        Project project = projectService.lambdaQuery()
                .eq(projectId != null, Project::getId, projectId)
                .one();
        ProjectVO projectVO = BeanUtil.copyProperties(project, ProjectVO.class);
        projectVO.setParticipantEmployees(employeeList);
        return Result.success(projectVO);
    }

    //更新项目表中的participant_count字段，正确计算每个项目的参与人数（重复参与算一个人）
    @PostMapping("/updateCount")
    public Result<Void> updateCount() {
        boolean success = projectService.updateCount();

        if (success) {
            return Result.success();
        } else {
            return Result.error("参与人员数量更新失败");
        }
    }


}
