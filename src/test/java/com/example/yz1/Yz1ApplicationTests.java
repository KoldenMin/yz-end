package com.example.yz1;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.yz1.entity.EducationBackground;
import com.example.yz1.entity.User;
import com.example.yz1.mapper.UserMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Yz1ApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        MPJLambdaWrapper<User> wrapper = new MPJLambdaWrapper<User>()
                .select(User::getUsername, User::getRealName)
                .select(EducationBackground::getSchool, EducationBackground::getMajor)
                .leftJoin(EducationBackground.class, EducationBackground::getUserId, User::getId);

        Page<User> page = userMapper.selectJoinPage(new Page<User>(1, 5), User.class, wrapper);
        page.getRecords().forEach(System.out::println);
    }

}
