package com.example.yz1.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 项目Bug表
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project_bug")
public class ProjectBug implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Bug ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * bug标题
     */
    private String title;

    /**
     * 测试人员ID
     */
    private Long testerId;

    /**
     * bug类型
     */
    private String bugType;

    /**
     * 操作系统
     */
    private String system;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 步骤
     */
    private String step;

    /**
     * 相关需求
     */
    private String requirement;

    /**
     * 指派人员ID
     */
    private Long assigneeId;

    /**
     * 是否解决
     */
    private Boolean isResolved;

    /**
     * 解决方案
     */
    private String solution;

    /**
     * 是否确认
     */
    private Boolean isConfirmed;

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
