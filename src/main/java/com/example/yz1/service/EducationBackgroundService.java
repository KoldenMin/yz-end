package com.example.yz1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yz1.dto.EducationBackgroundDTO;
import com.example.yz1.entity.EducationBackground;

import java.util.List;


/**
 * 教育背景服务接口
 */
public interface EducationBackgroundService extends IService<EducationBackground> {

    /**
     * 添加教育背景
     *
     * @param userId 用户ID
     * @param dto    教育背景信息
     * @return 是否添加成功
     */
    boolean addEducation(Long userId, EducationBackgroundDTO dto);

    /**
     * 批量添加教育背景
     *
     * @param userId  用户ID
     * @param dtoList 教育背景信息列表
     * @return 是否添加成功
     */
    boolean batchAddEducation(Long userId, List<EducationBackgroundDTO> dtoList);

    /**
     * 获取用户教育背景
     *
     * @param userId 用户ID
     * @return 教育背景列表
     */
    List<EducationBackground> listByUserId(Long userId);
}