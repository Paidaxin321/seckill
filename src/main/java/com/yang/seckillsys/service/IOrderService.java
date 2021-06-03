package com.yang.seckillsys.service;

import com.yang.seckillsys.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.seckillsys.pojo.User;
import com.yang.seckillsys.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YANGLIYUAN
 * @since 2021-04-17
 */
public interface IOrderService extends IService<Order> {

    Order seckill(User user, GoodsVo goods);
}
