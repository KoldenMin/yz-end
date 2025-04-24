package com.example.yz1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yz1.dto.LoginDTO;
import com.example.yz1.dto.RegisterDTO;
import com.example.yz1.dto.UserUpdateDTO;
import com.example.yz1.entity.User;
import com.example.yz1.vo.UserInfoVO;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 注册结果
     */
    boolean register(RegisterDTO registerDTO);

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 用户信息
     */
    UserInfoVO login(LoginDTO loginDTO);

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfoVO getUserInfo(Long userId);

    /**
     * 获取所有用户信息（管理员功能）
     *
     * @return 用户列表
     */
    List<User> listAllUsers();

    Boolean logout(Long userId);

    Boolean deleteById(Integer id);


    Boolean updateUser(Long id, UserUpdateDTO userUpdateDTO);

    Boolean setAsAdmin(Integer id);
}