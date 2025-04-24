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
 * 注册审核表
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("registration_approval")
public class RegistrationApproval implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 审核人ID
     */
    private Long approverId;

    /**
     * 审核状态，0待审核，1审核通过，2审核拒绝
     */
    private Integer status;

    /**
     * 审核意见
     */
    private String approvalOpinion;

    /**
     * 审核时间
     */
    private LocalDateTime approvalTime;

    /**
     * 指定转入部门ID
     */
    private Long targetDepartmentId;

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
