package com.example.yz1.controller;

import com.example.yz1.common.Result;
import com.example.yz1.dto.LoginDTO;
import com.example.yz1.dto.RegisterDTO;
import com.example.yz1.entity.User;
import com.example.yz1.service.UserService;
import com.example.yz1.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Void> register(@Validated @RequestBody RegisterDTO registerDTO) {
        boolean result = userService.register(registerDTO);
        return result ? Result.success() : Result.error("注册失败");
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<UserInfoVO> login(@Validated @RequestBody LoginDTO loginDTO) {
        UserInfoVO userInfo = userService.login(loginDTO);
        return Result.success(userInfo);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        UserInfoVO userInfo = userService.getUserInfo(userId);
        return Result.success(userInfo);
    }

    /**
     * 获取所有用户信息（管理员功能）
     */
    @GetMapping("/list")
    public Result<List<User>> listAllUsers(HttpServletRequest request) {
        Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
        if (!isAdmin) {
            return Result.error(403, "无权限");
        }

        List<User> userList = userService.listAllUsers();
        return Result.success(userList);
    }
}