package com.example.yz1.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 登录数据传输对象
 */
@Data
public class LoginDTO {
    
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
    
    /**
     * 验证码（前端验证，后端不处理）
     */
    private String captcha;
}