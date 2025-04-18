package com.example.yz1.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.yz1.common.Result;
import com.example.yz1.dto.LoginDTO;
import com.example.yz1.dto.RegisterDTO;
import com.example.yz1.dto.UpdateDTO;
import com.example.yz1.entity.User;
import com.example.yz1.service.UserService;
import com.example.yz1.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Value("${file.upload.path:uploads/avatars}")
    private String uploadPath;
    @Value("${file.upload.resume.path:uploads/resumes}")
    private String resumeUploadPath;

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
     * 用户登出
     */
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Boolean result = userService.logout(userId);
        if (result) {
            return Result.success();
        } else {
            return Result.error("登出失败");
        }
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
//    @GetMapping("/list")
//    public Result<List<User>> listAllUsers(HttpServletRequest request) {
//        Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
//        if (!isAdmin) {
//            return Result.error(403, "无权限");
//        }
//
//        List<User> userList = userService.listAllUsers();
//        return Result.success(userList);
//    }
    @GetMapping("/list")
    public Result<List<UserInfoVO>> listAllUsers(HttpServletRequest request) {
        Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
        if (!isAdmin) {
            return Result.error(403, "无权限");
        }

        List<User> userList = userService.listAllUsers();
        List<UserInfoVO> userInfoVOS = BeanUtil.copyToList(userList, UserInfoVO.class);
        return Result.success(userInfoVOS);

    }

    @GetMapping("/page")
    public Result<IPage<User>> getUserPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                           @RequestParam(defaultValue = "5") Integer pageSize,
                                           @RequestParam(required = false) String username,
                                           @RequestParam(required = false) String realName) {
        Page<User> page = new Page<>(pageNum, pageSize);
        Page<User> userPage = userService.lambdaQuery().like(StrUtil.isNotEmpty(username), User::getUsername, username)
                .like(StrUtil.isNotEmpty(realName), User::getRealName, realName).page(page);
        return Result.success(userPage);

    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        Boolean success = userService.deleteById(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }

    @PutMapping("/update/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody UpdateDTO updateDTO) {
        Boolean success = userService.updateUser(id, updateDTO);
        if (success) {
            return Result.success();
        } else {
            return Result.error("更新失败");
        }
    }

    @PutMapping("/setAsAdmin/{id}")
    public Result<Void> setAsAdmin(@PathVariable Integer id) {
        Boolean success = userService.setAsAdmin(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("设置失败");
        }
    }

    @GetMapping("/salt")
    public Result<String> getUserSalt(@RequestParam String username) {
        User user = userService.lambdaQuery().eq(User::getUsername, username).one();
        if (user == null) {
            // 为了安全考虑，即使用户不存在也返回一个随机salt，以防被利用来探测用户是否存在
            return Result.error("用户不存在");
        }
        return Result.success(user.getSalt());
    }

    /**
     * 上传头像
     *
     * @param file 头像文件
     * @return 结果
     */
    @PostMapping("/update-avatar")
    public Result<?> updateAvatar(@RequestParam("avatar") MultipartFile file,
                                  @RequestParam("userId") Long userId) {
        // 检查文件是否为空
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }

        try {
            // 确保上传目录存在
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String newFilename = "avatar_" + userId + "_" + UUID.randomUUID().toString().substring(0, 8) + fileExtension;

            // 保存文件
            File destFile = new File(uploadDir.getAbsolutePath() + File.separator + newFilename);
            file.transferTo(destFile);

            // 生成访问URL
            String avatarUrl = "/avatars/" + newFilename;
            // 在 UserController 中修改
//            String avatarUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/api/avatars/" + newFilename;

            // 更新用户表中的头像字段
//            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
//            updateWrapper.eq("id", userId);
//            updateWrapper.set("avatar", avatarUrl);
//            userService.update(updateWrapper);

            userService.lambdaUpdate()
                    .eq(User::getId, userId)
                    .set(User::getAvatar, avatarUrl)
                    .update();

            // 返回头像URL
            Map<String, String> result = new HashMap<>();
            result.put("avatarUrl", avatarUrl);

            return Result.success(result);

        } catch (IOException e) {
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 上传简历
     *
     * @param file   简历文件
     * @param userId 用户ID
     * @return 结果
     */
    @PostMapping("/upload-resume")
    public Result<?> uploadResume(@RequestParam("resume") MultipartFile file,
                                  @RequestParam("userId") Long userId) {
        // 检查文件是否为空
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }

        try {
            // 确保上传目录存在
            File uploadDir = new File(resumeUploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 获取原始文件名和扩展名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                return Result.error("文件名无效");
            }

            // 验证文件类型
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            if (!".pdf".equals(fileExtension) && !".doc".equals(fileExtension) && !".docx".equals(fileExtension)) {
                return Result.error("仅支持PDF、DOC或DOCX格式的简历");
            }

            // 生成唯一文件名
            String newFilename = "resume_" + userId + "_" + UUID.randomUUID().toString().substring(0, 8) + fileExtension;

            // 保存文件
            File destFile = new File(uploadDir.getAbsolutePath() + File.separator + newFilename);
            file.transferTo(destFile);

            // 生成访问URL
            String resumeUrl = "/resumes/" + newFilename;

            // 更新用户表中的简历字段
            userService.lambdaUpdate()
                    .eq(User::getId, userId)
                    .set(User::getResumeUrl, resumeUrl)
                    .update();

            // 返回简历URL
            Map<String, String> result = new HashMap<>();
            result.put("resumeUrl", resumeUrl);

            return Result.success(result);

        } catch (IOException e) {
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }
}