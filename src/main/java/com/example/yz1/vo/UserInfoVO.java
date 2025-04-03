package com.example.yz1.vo;

import com.example.yz1.entity.EducationBackground;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户信息视图对象
 */
@Data
public class UserInfoVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
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
    private Date entryTime;
    
    /**
     * 简历上传地址
     */
    private String resumeUrl;
    
    /**
     * 职能描述
     */
    private String jobDescription;
    
    /**
     * 是否管理员
     */
    private Boolean isAdmin;
    
    /**
     * 教育背景列表
     */
    private List<EducationBackground> educationList;
    
    /**
     * Token值
     */
    private String token;
}