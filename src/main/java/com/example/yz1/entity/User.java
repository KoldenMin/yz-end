package com.example.yz1.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类
 */
@Data
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码，JSON序列化时忽略
     */
    @JsonIgnore
    private String password;

    /**
     * 密码盐，JSON序列化时忽略
     */
    @JsonIgnore
    private String salt;

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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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
     * 是否管理员：0-否，1-是
     */
    private Integer isAdmin;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @TableLogic
    private Integer isDelete;
}