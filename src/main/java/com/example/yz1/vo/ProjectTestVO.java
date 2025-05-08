package com.example.yz1.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 项目测试表
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProjectTestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 测试ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 项目名称
     */
    private String projectName;


    /**
     * 测试用例名称
     */
    private String testName;

    /**
     * 测试人员ID
     */
    private Long testerId;

    /**
     * 测试人员姓名
     */
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


}
