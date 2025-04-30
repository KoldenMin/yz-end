package com.example.yz1.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.yz1.common.Result;
import com.example.yz1.entity.Project;
import com.example.yz1.service.IProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/addOrUpdate")
    public Result<Void> addOrUpdate(@RequestBody Project project) {
        boolean success = projectService.saveOrUpdate(project);
        if (success) {
            return Result.success();
        } else {
            return Result.error("新增项目失败");
        }
    }

    @GetMapping("/info/{id}")
    public Result<Project> info(@PathVariable("id") Long id) {
        return Result.success(projectService.getById(id));
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteProjectByID(@PathVariable("id") Long id) {
        boolean success = projectService.removeById(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除项目失败");
        }
    }

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


}
