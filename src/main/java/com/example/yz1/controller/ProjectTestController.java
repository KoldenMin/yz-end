package com.example.yz1.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.yz1.common.Result;
import com.example.yz1.entity.Employee;
import com.example.yz1.entity.Project;
import com.example.yz1.entity.ProjectTest;
import com.example.yz1.mapper.ProjectTestMapper;
import com.example.yz1.service.IProjectTestService;
import com.example.yz1.vo.ProjectTestVO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 项目测试表 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/project-test")
public class ProjectTestController {
    private final IProjectTestService projectTestService;
    private final ProjectTestMapper projectTestMapper;

    // 新增、修改测试用例
    @PostMapping("/addOrUpdate")
    public Result<Void> addOrUpdate(@RequestBody ProjectTest projectTest) {
        boolean success = projectTestService.saveOrUpdate(projectTest);
        if (success) {
            return Result.success();
        } else {
            return Result.error("新增测试用例失败");
        }
    }

    // 根据id删除测试用例
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteProjectTestByID(@PathVariable("id") Long id) {
        boolean success = projectTestService.removeById(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除项目失败");
        }
    }

    // 根据id查看测试用例
    @GetMapping("/info/{id}")
    public Result<ProjectTestVO> info(@PathVariable("id") Long id) {
        MPJLambdaWrapper<ProjectTest> wrapper = new MPJLambdaWrapper<ProjectTest>()
                .selectAll(ProjectTest.class)
                .selectAs(Project::getName, ProjectTestVO::getProjectName)
                .leftJoin(Project.class, Project::getId, ProjectTest::getProjectId)
                .selectAs(Employee::getName, ProjectTestVO::getTesterName)
                .leftJoin(Employee.class, Employee::getId, ProjectTest::getTesterId)
                .eq(id != null, ProjectTest::getId, id);

        wrapper.disableSubLogicDel();

        ProjectTestVO projectTestVO = projectTestMapper.selectJoinOne(ProjectTestVO.class, wrapper);
        return Result.success(projectTestVO);
    }

    // 查看指定project_id的所有测试用例
    @GetMapping("/getByProjectId/{projectID}")
    public Result<List<ProjectTestVO>> getByProjectId(@PathVariable Long projectID) {
        MPJLambdaWrapper<ProjectTest> wrapper = new MPJLambdaWrapper<ProjectTest>()
                .selectAll(ProjectTest.class)
                .selectAs(Project::getName, ProjectTestVO::getProjectName)
                .leftJoin(Project.class, Project::getId, ProjectTest::getProjectId)
                .selectAs(Employee::getName, ProjectTestVO::getTesterName)
                .leftJoin(Employee.class, Employee::getId, ProjectTest::getTesterId)
                .eq(projectID != null, ProjectTest::getProjectId, projectID)
                .disableSubLogicDel();

        List<ProjectTestVO> projectTestVOS = projectTestMapper.selectJoinList(ProjectTestVO.class, wrapper);
        return Result.success(projectTestVOS);
    }

    // 分页
    @GetMapping("/page")
    public Result<IPage<ProjectTestVO>> getProjectTestPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                                           @RequestParam(defaultValue = "5") Integer pageSize,
                                                           @RequestParam(required = false) String testName,
                                                           @RequestParam(required = false) String testType) {
        MPJLambdaWrapper<ProjectTest> wrapper = new MPJLambdaWrapper<ProjectTest>()
                .selectAll(ProjectTest.class)
                .selectAs(Project::getName, ProjectTestVO::getProjectName)
                .leftJoin(Project.class, Project::getId, ProjectTest::getProjectId)
                .selectAs(Employee::getName, ProjectTestVO::getTesterName)
                .leftJoin(Employee.class, Employee::getId, ProjectTest::getTesterId)
                .like(testName != null, ProjectTest::getTestName, testName)
                .like(testType != null, ProjectTest::getTestType, testType)
                .disableSubLogicDel();

        Page<ProjectTestVO> page = new Page<>(pageNum, pageSize);
        Page<ProjectTestVO> newPage = projectTestMapper.selectJoinPage(page, ProjectTestVO.class, wrapper);
        return Result.success(newPage);
    }

    // 查看所有测试用例
    @GetMapping("/list")
    public Result<List<ProjectTestVO>> listAll() {
        MPJLambdaWrapper<ProjectTest> wrapper = new MPJLambdaWrapper<ProjectTest>()
                .selectAll(ProjectTest.class)
                .selectAs(Project::getName, ProjectTestVO::getProjectName)
                .leftJoin(Project.class, Project::getId, ProjectTest::getProjectId)
                .selectAs(Employee::getName, ProjectTestVO::getTesterName)
                .leftJoin(Employee.class, Employee::getId, ProjectTest::getTesterId)
                .disableSubLogicDel();

        List<ProjectTestVO> projectTestVOS = projectTestMapper.selectJoinList(ProjectTestVO.class, wrapper);
        return Result.success(projectTestVOS);
    }

}
