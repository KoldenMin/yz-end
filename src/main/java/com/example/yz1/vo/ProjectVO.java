package com.example.yz1.vo;

import com.example.yz1.entity.Employee;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 项目表
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@Data
public class ProjectVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目ID
     */
    private Long id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目起止日期-开始
     */
    private LocalDate startDate;

    /**
     * 项目起止日期-结束
     */
    private LocalDate endDate;

    /**
     * 项目概述
     */
    private String description;

    /**
     * 参与人员个数
     */
    private Integer participantCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 项目参与人员
     */
    private List<Employee> participantEmployees;


}
