package com.example.yz1.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

@Data
public class UserUpdateDTO {
    /**
     * 用户名
     */
    @Size(min = 2, max = 20, message = "用户名长度必须在2-20个字符之间")
    private String username;

    /**
     * 密码
     */
    @Size(min = 2, max = 20, message = "密码长度必须在2-20个字符之间")
    private String password;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 性别：0-女，1-男
     */
    private Integer gender;

    /**
     * 年龄
     */
    @Min(value = 1, message = "年龄必须大于或等于18岁")
    @Max(value = 100, message = "年龄必须小于或等于100岁")
    private Integer age;

    /**
     * 电话
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;

    /**
     * 当前住址
     */
    private String currentAddress;

    /**
     * 入职时间
     */
//    private Date entryTime;

    /**
     * 职能描述
     */
//    private String jobDescription;

    /**
     * 教育背景列表
     */
    private List<EducationBackgroundDTO> educationList;
}
