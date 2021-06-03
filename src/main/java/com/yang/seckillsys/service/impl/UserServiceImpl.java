package com.yang.seckillsys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yang.seckillsys.pojo.User;
import com.yang.seckillsys.mapper.UserMapper;
import com.yang.seckillsys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.seckillsys.utils.CookieUtil;
import com.yang.seckillsys.utils.MD5Util;
import com.yang.seckillsys.utils.UUIDUtil;
import com.yang.seckillsys.vo.LoginVo;
import com.yang.seckillsys.vo.RespBean;
import com.yang.seckillsys.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YANGLIYUAN
 * @since 2021-04-12
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

/*    public RespBean dologin(String mobile, String password, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("nickname", mobile);
        User user = userMapper.selectOne(wrapper);
        String dbPass = MD5Util.formPassToDBPass(password, MD5Util.salt);
        if (user == null || !user.getPassword().equals(dbPass) ) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        } else {
            //生成cookie
            String ticket = UUIDUtil.uuid();
            //将用户信息存入 redis 中
            redisTemplate.opsForValue().set("user:" + ticket, user);
            System.out.println(user);       //控制台提示，可删掉
            System.out.println("----" + ticket);        //控制台提示，可删掉
            CookieUtil.setCookie(request, response, "userTicket", ticket);
            return RespBean.success();
        }

    }*/

    //根据 cookie 获取用户
    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        if (user != null) {
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }

    @Override
    public RespBean dologin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("nickname", mobile);

        //根据手机号获取用户
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        //判断密码是否正确
        if(!MD5Util.formPassToDBPass(password,user.getSlat()).equals(user.getPassword())) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        //生成cookie
        String ticket = UUIDUtil.uuid();
        //将用户信息存入 redis 中
        redisTemplate.opsForValue().set("user:" + ticket, user);
        CookieUtil.setCookie(request, response, "userTicket", ticket);
        return RespBean.success(ticket);
    }


    //测试用（批量添加user对象到Redis中）
    @Override
    public RespBean dologinUtil() {
        List<User> users = new ArrayList<>(20000);
        for (int i = 1; i <= 20000; i++) {
            User user = new User();
            user.setId((long)i);
            user.setNickname((13000000000L+i)+"");
            user.setSlat("1a2b3c4d");
            user.setHead("无");
            user.setPassword(MD5Util.inputPassToDBPass("123456",user.getSlat()));
            users.add(user);
        }
        System.out.println("create user");

        for (User user : users) {
            //生成cookie
            String ticket = UUIDUtil.uuid();
            //将用户信息存入 redis 中
            redisTemplate.opsForValue().set("user:" + ticket, user);
            System.out.println(user.getNickname() + "," + ticket);       //控制台提示
        }

        return RespBean.success();
    }

}
