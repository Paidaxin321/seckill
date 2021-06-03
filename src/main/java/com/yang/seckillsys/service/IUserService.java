package com.yang.seckillsys.service;

import com.yang.seckillsys.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.seckillsys.vo.LoginVo;
import com.yang.seckillsys.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YANGLIYUAN
 * @since 2021-04-12
 */
public interface IUserService extends IService<User> {

    //根据 cookie 获取用户
    User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);

    RespBean dologin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    //测试用（批量添加user对象到Redis中，由于需要用到springboot框架中的RedisTemplate，就并没有单独写工具类）
    RespBean dologinUtil();
}
