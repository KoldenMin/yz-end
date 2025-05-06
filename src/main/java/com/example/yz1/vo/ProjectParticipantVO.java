package com.example.yz1.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 项目参与人员表
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@Data
@Accessors(chain = true)
public class ProjectParticipantVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
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
     * 员工ID
     */
    private Long employeeId;

    /**
     * 员工姓名
     */
    private String employeeName;

    /**
     * 功能模块名称
     */
    private String moduleName;

    /**
     * 相关需求
     */
    private String requirement;

    /**
     * 任务状态
     */
    private String taskStatus;

    /**
     * 起止日期
     */
    private LocalDate startDate;

    /**
     * 附件地址
     */
    private String attachmentUrl;

    /**
     * 图片地址
     */
    private String imageUrl;


}
