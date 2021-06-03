package com.yang.seckillsys.controller;

import com.yang.seckillsys.service.impl.UserServiceImpl;
import com.yang.seckillsys.vo.LoginVo;
import com.yang.seckillsys.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@Slf4j
public class LoginController {

    @Autowired
    private UserServiceImpl userService;
    @RequestMapping({"/login","/"})
    public String login(){
      return "login";
    };

/*    @RequestMapping("/dologin")
    @ResponseBody
    public RespBean dologin(@RequestParam("mobile") String mobile, @RequestParam("password") String password
            ,HttpServletRequest request,HttpServletResponse respon){
       log.info("进入dologin方法");
       return userService.dologin(mobile, password, request,respon);
    }*/

    @RequestMapping("/dologin")
    @ResponseBody
    public RespBean dologin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){
        //log.info("{}",loginVo);
        return userService.dologin(loginVo,request,response);
    }

    //测试用（批量添加user对象到Redis中，需要通过浏览器访问调用该接口方法）
    @RequestMapping("/dologinUtil")
    @ResponseBody
    public RespBean dologinUtil(){
        return userService.dologinUtil();
    }

}
