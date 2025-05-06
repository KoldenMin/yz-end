package com.example.yz1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.yz1.entity.Project;
import com.example.yz1.entity.ProjectParticipant;
import com.example.yz1.mapper.ProjectMapper;
import com.example.yz1.mapper.ProjectParticipantMapper;
import com.example.yz1.service.IProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目表 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-04-21
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

    @Autowired
    private ProjectParticipantMapper projectParticipantMapper;


    @Override
    public boolean updateCount() {
        QueryWrapper<ProjectParticipant> wrapper = new QueryWrapper<ProjectParticipant>()
                .select("project_id", "count(distinct employee_id) as participantCount")
                .groupBy("project_id");
        List<Map<String, Object>> maps = projectParticipantMapper.selectMaps(wrapper);
        // 更新之前先把所有数量改为0，防止项目剩余1人导致删不掉
        this.lambdaUpdate().set(Project::getParticipantCount, 0).update();
        for (Map<String, Object> map : maps) {
            Long projectId = (Long) map.get("project_id");
            Object participantCount = map.get("participantCount");
            this.lambdaUpdate()
                    .set(Project::getParticipantCount, participantCount)
                    .eq(Project::getId, projectId)
                    .update();
        }
        return true;
    }
}
