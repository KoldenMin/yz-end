package com.example.yz1.dto;

import lombok.Data;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@Data
public class DepartmentUpdateDTO {

    /**
     * 部门职能
     */
    private String depaFunction;


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


}
