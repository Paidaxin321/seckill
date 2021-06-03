package com.yang.seckillsys;

import com.yang.seckillsys.mapper.UserMapper;
import com.yang.seckillsys.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;

@SpringBootTest
class SeckillSysApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Test
    void contextLoads() {
        System.out.println("qqq");
        HashMap<String,Object> map = new HashMap<>();
        map.put("nickname","1512926469");
        List<User> users = userMapper.selectByMap(map);
    }


}
