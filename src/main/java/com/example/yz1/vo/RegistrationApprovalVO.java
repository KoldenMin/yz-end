package com.example.yz1.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 注册审核表
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@Data
public class RegistrationApprovalVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 注册用户ID
     */
    private Long userId;

    /**
     * 注册用户姓名
     */
    private String realName;


    /**
     * 审核状态，0待审核，1审核通过，2审核拒绝
     */
    private Integer status;

    /**
     * 审核人姓名
     */
    private String employeeName;

    /**
     * 审核意见
     */
    private String approvalOpinion;

    /**
     * 审核时间
     */
    private LocalDateTime approvalTime;

    /**
     * 指定转入部门名称
     */
    private String departmentName;


}
