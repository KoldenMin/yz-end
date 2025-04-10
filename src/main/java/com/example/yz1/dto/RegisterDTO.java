package com.example.yz1.dto;

import lombok.Data;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

/**
 * 用户注册数据传输对象
 */
@Data
public class RegisterDTO {
    
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度必须在2-20个字符之间")
    private String username;
    
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 2, max = 20, message = "密码长度必须在2-20个字符之间")
    private String password;
    
    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String realName;
    
    /**
     * 性别：0-女，1-男
     */
    @NotNull(message = "性别不能为空")
    private Integer gender;
    
    /**
     * 年龄
     */
    @NotNull(message = "年龄不能为空")
    @Min(value = 1, message = "年龄必须大于或等于18岁")
    @Max(value = 100, message = "年龄必须小于或等于100岁")
    private Integer age;
    
    /**
     * 电话
     */
    @NotBlank(message = "电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;
    
    /**
     * 当前住址
     */
    private String currentAddress;
    
    /**
     * 入职时间
     */
    private Date entryTime;
    
    /**
     * 职能描述
     */
    private String jobDescription;
    
    /**
     * 教育背景列表
     */
    @NotEmpty(message = "教育背景不能为空")
    private List<EducationBackgroundDTO> educationList;
}