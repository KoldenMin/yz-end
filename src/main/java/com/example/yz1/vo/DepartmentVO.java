package com.example.yz1.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@Data
public class DepartmentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 部门职能
     */
    private String depaFunction;

    /**
     * 部门人数
     */
    private Integer employeeCount;

    /**
     * 部门工作日期
     */
    private String workingDate;

    /**
     * 部门工作时长
     */
    private String workingHours;

    /**
     * 部门负责人ID(权限设置)
     */
    private Long managerId;

    /**
     * 部门负责人姓名
     */
    private String managerName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
