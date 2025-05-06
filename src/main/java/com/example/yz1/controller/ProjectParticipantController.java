package com.example.yz1.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.yz1.common.Result;
import com.example.yz1.entity.Employee;
import com.example.yz1.entity.Project;
import com.example.yz1.entity.ProjectParticipant;
import com.example.yz1.mapper.ProjectParticipantMapper;
import com.example.yz1.service.IProjectParticipantService;
import com.example.yz1.service.IProjectService;
import com.example.yz1.vo.ProjectParticipantVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 项目参与人员表 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/project-participant")
public class ProjectParticipantController {

    private final IProjectParticipantService projectParticipantService;
    private final ProjectParticipantMapper projectParticipantMapper;
    private final IProjectService projectService;

    // 分页
    @GetMapping("/page")
    public Result<IPage<ProjectParticipantVO>> getProjectParticipantPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                                                         @RequestParam(defaultValue = "5") Integer pageSize,
                                                                         @RequestParam(required = false) String moduleName,
                                                                         @RequestParam(required = false) String requirement) {
        projectService.updateCount();

        MPJLambdaWrapper<ProjectParticipant> wrapper = new MPJLambdaWrapper<ProjectParticipant>()
                .selectAll(ProjectParticipant.class)
                .selectAs(Project::getName, ProjectParticipantVO::getProjectName)
                .leftJoin(Project.class, Project::getId, ProjectParticipant::getProjectId)
                .selectAs(Employee::getName, ProjectParticipantVO::getEmployeeName)
                .leftJoin(Employee.class, Employee::getId, ProjectParticipant::getEmployeeId);

        wrapper.disableSubLogicDel();

        wrapper.like(StrUtil.isNotEmpty(moduleName), ProjectParticipant::getModuleName, moduleName)
                .like(StrUtil.isNotEmpty(requirement), ProjectParticipant::getRequirement, requirement);

        Page<ProjectParticipantVO> page = new Page<>(pageNum, pageSize);

        Page<ProjectParticipantVO> projectParticipantVOPage = projectParticipantMapper.selectJoinPage(page, ProjectParticipantVO.class, wrapper);
        return Result.success(projectParticipantVOPage);
    }

    // 根据project_id查询参与项目的所有员工
    @GetMapping("/selectByProjectId/{projectId}")
    public Result<List<ProjectParticipantVO>> getProjectParticipantByProjectId(@PathVariable Long projectId) {
        MPJLambdaWrapper<ProjectParticipant> wrapper = new MPJLambdaWrapper<ProjectParticipant>()
                .eq(projectId != null, ProjectParticipant::getProjectId, projectId)
                .selectAll(ProjectParticipant.class)
                .selectAs(Project::getName, ProjectParticipantVO::getProjectName)
                .leftJoin(Project.class, Project::getId, ProjectParticipant::getProjectId)
                .selectAs(Employee::getName, ProjectParticipantVO::getEmployeeName)
                .leftJoin(Employee.class, Employee::getId, ProjectParticipant::getEmployeeId);

        wrapper.disableSubLogicDel();

        List<ProjectParticipantVO> voList = projectParticipantMapper.selectJoinList(ProjectParticipantVO.class, wrapper);
        return Result.success(voList);
    }

    // 根据project_id查询参与项目的所有员工
    @GetMapping("/info/{id}")
    public Result<ProjectParticipantVO> getInfo(@PathVariable Long id) {
        MPJLambdaWrapper<ProjectParticipant> wrapper = new MPJLambdaWrapper<ProjectParticipant>()
                .eq(id != null, ProjectParticipant::getId, id)
                .selectAll(ProjectParticipant.class)
                .selectAs(Project::getName, ProjectParticipantVO::getProjectName)
                .leftJoin(Project.class, Project::getId, ProjectParticipant::getProjectId)
                .selectAs(Employee::getName, ProjectParticipantVO::getEmployeeName)
                .leftJoin(Employee.class, Employee::getId, ProjectParticipant::getEmployeeId);

        wrapper.disableSubLogicDel();

        ProjectParticipantVO projectParticipantVO = projectParticipantMapper.selectJoinOne(ProjectParticipantVO.class, wrapper);

        return Result.success(projectParticipantVO);
    }

    // 新增或修改参与信息
    @PostMapping("/addOrUpdate")
    public Result<Void> addOrUpdate(@RequestBody ProjectParticipant projectParticipant) {
        boolean success = projectParticipantService.saveOrUpdate(projectParticipant);
        if (success) {
            projectService.updateCount();
            return Result.success();
        } else {
            return Result.error("新增或修改失败");
        }
    }

    // 根据id删除
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteById(@PathVariable Long id) {
        boolean success = projectParticipantService.removeById(id);
        if (success) {
            projectService.updateCount();
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }


}
