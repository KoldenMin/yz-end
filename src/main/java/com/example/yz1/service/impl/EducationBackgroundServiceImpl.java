package com.example.yz1.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yz1.dto.EducationBackgroundDTO;
import com.example.yz1.entity.EducationBackground;
import com.example.yz1.mapper.EducationBackgroundMapper;
import com.example.yz1.service.EducationBackgroundService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 教育背景服务实现类
 */
@Service
public class EducationBackgroundServiceImpl extends ServiceImpl<EducationBackgroundMapper, EducationBackground> implements EducationBackgroundService {
    
    /**
     * 添加教育背景
     *
     * @param userId 用户ID
     * @param dto 教育背景信息
     * @return 是否添加成功
     */
    @Override
    public boolean addEducation(Long userId, EducationBackgroundDTO dto) {
        EducationBackground entity = new EducationBackground();
        BeanUtil.copyProperties(dto, entity);
        
        entity.setUserId(userId);
        Date now = new Date();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        
        return this.save(entity);
    }
    
    /**
     * 批量添加教育背景
     *
     * @param userId 用户ID
     * @param dtoList 教育背景信息列表
     * @return 是否添加成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchAddEducation(Long userId, List<EducationBackgroundDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return true;
        }
        
        List<EducationBackground> entityList = new ArrayList<>(dtoList.size());
        Date now = new Date();
        
        for (EducationBackgroundDTO dto : dtoList) {
            EducationBackground entity = new EducationBackground();
            BeanUtil.copyProperties(dto, entity);
            entity.setUserId(userId);
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
            entityList.add(entity);
        }
        
        return this.saveBatch(entityList);
    }
    
    /**
     * 获取用户教育背景
     *
     * @param userId 用户ID
     * @return 教育背景列表
     */
    @Override
    public List<EducationBackground> listByUserId(Long userId) {
        LambdaQueryWrapper<EducationBackground> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EducationBackground::getUserId, userId);
        queryWrapper.orderByAsc(EducationBackground::getStartDate);
        
        return this.list(queryWrapper);
    }
}