package com.yang.seckillsys.controller;


import com.yang.seckillsys.pojo.User;
import com.yang.seckillsys.service.IGoodsService;
import com.yang.seckillsys.service.IUserService;
import com.yang.seckillsys.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author YANGLIYUAN
 * @since 2021-04-17
 */
@Controller
@Slf4j
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;


    /*进入商品列表界面*/
    // 通过 WebConfig 和 UserArgumentResolver 类简化后的 toList 方法，以后写请求方法都采用这种格式
    /*性能优化之页面缓存*/
    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    /*一定记得添加这个注释*/
    @ResponseBody
    public String toList(Model model, User user, HttpServletRequest request, HttpServletResponse response) {
        //若 user 对象为空，则返回登录页
        if (user == null) {
            WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
           String login = thymeleafViewResolver.getTemplateEngine().process("login", webContext);
           return login;
        }
        /*redis中获取页面*/
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        /*如果为空，手动渲染,存入redis并且返回*/
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", webContext);
       if (!StringUtils.isEmpty(html)){
           valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
       }
        return html;
    }

    /* 跳转商品详情页 */
    @RequestMapping(value = "/toDetail/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail(Model model, User user, @PathVariable Long goodsId,HttpServletRequest request,HttpServletResponse response) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail:" + goodsId);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        log.info(startDate.toString());
        Date endDate = goodsVo.getEndDate();
        log.info(endDate.toString());
        Date nowDate = new Date();
        System.out.println(nowDate);
        //秒杀状态
        int secKillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;

        if (nowDate.before(startDate)) {
            //秒杀还未开始
            remainSeconds = ((int) ((startDate.getTime() - nowDate.getTime()) / 1000));
        } else if (nowDate.after(endDate)) {
            //秒杀已结束
            secKillStatus = 2;
            remainSeconds = -1;
        } else {
            //秒杀中
            secKillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("secKillStatus", secKillStatus);
        model.addAttribute("goods", goodsVo);
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", webContext);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsDetail:"+goodsId,html,60,TimeUnit.SECONDS);
        }
        return html;
        // return "goodsDetail";
    }
}
