package com.example.yz1.service;

import com.example.yz1.entity.Project;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 项目表 服务类
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
public interface IProjectService extends IService<Project> {

    boolean updateCount();
}
