package com.example.yz1.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.yz1.common.Result;
import com.example.yz1.entity.Employee;
import com.example.yz1.entity.Project;
import com.example.yz1.entity.ProjectBug;
import com.example.yz1.mapper.ProjectBugMapper;
import com.example.yz1.service.IProjectBugService;
import com.example.yz1.vo.ProjectBugVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 项目Bug表 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/project-bug")
public class ProjectBugController {
    private final IProjectBugService projectBugService;
    private final ProjectBugMapper projectBugMapper;

    // 新增、修改bug列表
    @PostMapping("/addOrUpdate")
    public Result<Void> addOrUpdate(@RequestBody ProjectBug projectBug) {
        boolean success = projectBugService.saveOrUpdate(projectBug);
        if (success) {
            return Result.success();
        } else {
            return Result.error("新增bug失败");
        }
    }

    // 根据id删除bug列表
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteProjectBugByID(@PathVariable("id") Long id) {
        boolean success = projectBugService.removeById(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除bug失败");
        }
    }

    // 根据id查看bug用例
    @GetMapping("/info/{id}")
    public Result<ProjectBugVO> info(@PathVariable("id") Long id) {
        MPJLambdaWrapper<ProjectBug> wrapper = new MPJLambdaWrapper<ProjectBug>()
                .selectAll(ProjectBug.class)
                .selectAs(Project::getName, ProjectBugVO::getProjectName)
                .leftJoin(Project.class, Project::getId, ProjectBug::getProjectId)
                .selectAs(Employee::getName, ProjectBugVO::getTesterName)
                .leftJoin(Employee.class, Employee::getId, ProjectBug::getTesterId)
                .eq(id != null, ProjectBug::getId, id)
                .disableSubLogicDel();


        ProjectBugVO projectBugVO = projectBugMapper.selectJoinOne(ProjectBugVO.class, wrapper);
        return Result.success(projectBugVO);
    }

    // 查看指定project_id的所有bug列表
    @GetMapping("/getByProjectId/{projectID}")
    public Result<List<ProjectBugVO>> getBugByProjectId(@PathVariable Long projectID) {
        MPJLambdaWrapper<ProjectBug> wrapper = new MPJLambdaWrapper<ProjectBug>()
                .selectAll(ProjectBug.class)
                .selectAs(Project::getName, ProjectBugVO::getProjectName)
                .leftJoin(Project.class, Project::getId, ProjectBug::getProjectId)
                .selectAs(Employee::getName, ProjectBugVO::getTesterName)
                .leftJoin(Employee.class, Employee::getId, ProjectBug::getTesterId)
                .eq(projectID != null, ProjectBug::getProjectId, projectID)
                .disableSubLogicDel();

        List<ProjectBugVO> projectBugVOS = projectBugMapper.selectJoinList(ProjectBugVO.class, wrapper);
        return Result.success(projectBugVOS);
    }

    // 分页
    @GetMapping("/page")
    public Result<IPage<ProjectBugVO>> getProjectBugPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                                         @RequestParam(defaultValue = "5") Integer pageSize,
                                                         @RequestParam(required = false) String title,
                                                         @RequestParam(required = false) String bugType) {
        MPJLambdaWrapper<ProjectBug> wrapper = new MPJLambdaWrapper<ProjectBug>()
                .selectAll(ProjectBug.class)
                .selectAs(Project::getName, ProjectBugVO::getProjectName)
                .leftJoin(Project.class, Project::getId, ProjectBug::getProjectId)
                .selectAs(Employee::getName, ProjectBugVO::getTesterName)
                .leftJoin(Employee.class, Employee::getId, ProjectBug::getTesterId)
                .like(title != null, ProjectBug::getTitle, title)
                .like(bugType != null, ProjectBug::getBugType, bugType)
                .disableSubLogicDel();

        Page<ProjectBugVO> page = projectBugMapper.selectJoinPage(new Page<>(pageNum, pageSize), ProjectBugVO.class, wrapper);
        return Result.success(page);
    }

    // 查看所有bug
    @GetMapping("/list")
    public Result<List<ProjectBugVO>> listAll() {
        MPJLambdaWrapper<ProjectBug> wrapper = new MPJLambdaWrapper<ProjectBug>()
                .selectAll(ProjectBug.class)
                .selectAs(Project::getName, ProjectBugVO::getProjectName)
                .leftJoin(Project.class, Project::getId, ProjectBug::getProjectId)
                .selectAs(Employee::getName, ProjectBugVO::getTesterName)
                .leftJoin(Employee.class, Employee::getId, ProjectBug::getTesterId)
                .disableSubLogicDel();

        List<ProjectBugVO> projectBugVOS = projectBugMapper.selectJoinList(ProjectBugVO.class, wrapper);
        return Result.success(projectBugVOS);
    }


}
