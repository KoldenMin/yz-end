package com.example.yz1.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 项目测试表
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@Data
public class ProjectTestDTO {
    /**
     * 测试ID
     */
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    private String projectName;

    /**
     * 测试用例名称
     */
    private String testName;

    /**
     * 测试人员ID
     */
    private Long testerId;

    private String testerName;

    /**
     * 测试类型
     */
    private String testType;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 状态
     */
    private String status;

    /**
     * 添加日期
     */
    private LocalDate addDate;

    /**
     * 步骤
     */
    private String step;

    /**
     * 预期结果
     */
    private String expectedResult;

    /**
     * 实际结果
     */
    private String actualResult;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDelete;


}
