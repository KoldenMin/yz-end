package com.example.yz1.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yz1.dto.LoginDTO;
import com.example.yz1.dto.RegisterDTO;
import com.example.yz1.entity.EducationBackground;
import com.example.yz1.entity.User;
import com.example.yz1.mapper.UserMapper;
import com.example.yz1.service.EducationBackgroundService;
import com.example.yz1.service.UserService;
import com.example.yz1.utils.JwtUtil;
import com.example.yz1.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private EducationBackgroundService educationBackgroundService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 注册结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(RegisterDTO registerDTO) {
        // 1. 检查用户名是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, registerDTO.getUsername());
        if (this.count(queryWrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 2. 创建用户
        User user = new User();
        BeanUtil.copyProperties(registerDTO, user, "password", "educationList");

        // 3. 处理密码，生成盐值并加密
        String salt = UUID.randomUUID().toString().replace("-", "");
        String encryptedPassword = DigestUtil.md5Hex(registerDTO.getPassword() + salt);
        user.setSalt(salt);
        user.setPassword(encryptedPassword);

        // 4. 设置默认值
        user.setIsAdmin(0); // 默认为普通用户
        user.setStatus(1);  // 默认启用状态
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);

        // 5. 保存用户
        boolean saved = this.save(user);
        if (!saved) {
            throw new RuntimeException("用户注册失败");
        }

        // 6. 保存教育背景
        educationBackgroundService.batchAddEducation(user.getId(), registerDTO.getEducationList());

        return true;
    }

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 用户信息
     */
    @Override
    public UserInfoVO login(LoginDTO loginDTO) {
        // 1. 查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = this.getOne(queryWrapper);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 验证密码
        String encryptedPassword = DigestUtil.md5Hex(loginDTO.getPassword() + user.getSalt());
        if (!user.getPassword().equals(encryptedPassword)) {
            throw new RuntimeException("密码错误");
        }

        // 3. 检查用户状态
        if (user.getStatus() != 1) {
            throw new RuntimeException("账号已被禁用");
        }

        // 4. 生成token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getIsAdmin() == 1);

        // 5. 获取用户教育背景
        List<EducationBackground> educationList = educationBackgroundService.listByUserId(user.getId());

        // 6. 构建返回VO
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(user, userInfoVO);
        userInfoVO.setIsAdmin(user.getIsAdmin() == 1);
        userInfoVO.setEducationList(educationList);
        userInfoVO.setToken(token);

        return userInfoVO;
    }

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @Override
    public UserInfoVO getUserInfo(Long userId) {
        // 1. 获取用户
        User user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 获取用户教育背景
        List<EducationBackground> educationList = educationBackgroundService.listByUserId(userId);

        // 3. 构建返回VO
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(user, userInfoVO);
        userInfoVO.setIsAdmin(user.getIsAdmin() == 1);
        userInfoVO.setEducationList(educationList);

        return userInfoVO;
    }

    /**
     * 获取所有用户信息（管理员功能）
     *
     * @return 用户列表
     */
    @Override
    public List<User> listAllUsers() {
        return this.list();
    }
}