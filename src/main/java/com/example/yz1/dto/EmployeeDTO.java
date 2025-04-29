package com.example.yz1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息视图对象
 */
@Data
public class EmployeeDTO {

    private Long id;

    /**
     * 姓名
     */
    private String name;


    /**
     * 性别：0-女，1-男
     */
    private Integer gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 电话
     */
    private String phone;


    /**
     * 当前住址
     */
    private String currentAddress;

    /**
     * 入职时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entryTime;

    /**
     * 职能描述
     */
    private String jobDescription;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 角色
     */
    private Integer role;
}