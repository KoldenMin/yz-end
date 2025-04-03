package com.example.yz1.controller;

import com.example.yz1.common.Result;
import com.example.yz1.dto.EducationBackgroundDTO;
import com.example.yz1.entity.EducationBackground;
import com.example.yz1.service.EducationBackgroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 教育背景控制器
 */
@RestController
@RequestMapping("/education")
public class EducationController {
    
    @Autowired
    private EducationBackgroundService educationBackgroundService;
    
    /**
     * 添加教育背景
     */
    @PostMapping("/add")
    public Result<Void> addEducation(HttpServletRequest request, @Validated @RequestBody EducationBackgroundDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        boolean result = educationBackgroundService.addEducation(userId, dto);
        return result ? Result.success() : Result.error("添加教育背景失败");
    }
    
    /**
     * 获取当前用户教育背景
     */
    @GetMapping("/list")
    public Result<List<EducationBackground>> listEducation(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<EducationBackground> educationList = educationBackgroundService.listByUserId(userId);
        return Result.success(educationList);
    }
}