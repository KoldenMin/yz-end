package com.example.yz1.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 教育背景数据传输对象
 */
@Data
public class EducationBackgroundDTO {
    
    /**
     * 学校名称
     */
    @NotBlank(message = "学校名称不能为空")
    private String school;
    
    /**
     * 专业
     */
    @NotBlank(message = "专业不能为空")
    private String major;
    
    /**
     * 学位
     */
    private String degree;
    
    /**
     * 起止日期-开始
     */
    @NotNull(message = "开始日期不能为空")
    private Date startDate;
    
    /**
     * 起止日期-结束
     */
    private Date endDate;
    
    /**
     * 教育经历描述
     */
    private String description;
}