package com.example.yz1.vo;

import com.example.yz1.entity.EducationBackground;
import com.example.yz1.entity.Project;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息视图对象
 */
@Data
public class EmployeeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 员工ID
     */
    private Long id;
    /**
     * 关联的用户ID
     */
    private Long userId;

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
     * 头像URL
     */
    private String avatar;

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
     * 简历上传地址
     */
    private String resumeUrl;

    /**
     * 职能描述
     */
    private String jobDescription;

    /**
     * 部门id
     */
    private Integer departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 角色
     */
    private Integer role;


    /**
     * 教育背景列表
     */
    private List<EducationBackground> educationList;

    /**
     * Token值
     */
    private String token;

    /**
     * 参与项目列表
     */
    private List<Project> projectList;

    private String projectName;

    private String projectDescription;
}