package com.yang.seckillsys.activemq;

import com.yang.seckillsys.mapper.OrderMapper;
import com.yang.seckillsys.pojo.Order;
import com.yang.seckillsys.pojo.SeckillOrder;
import com.yang.seckillsys.service.ISeckillOrderService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/* 消费者 */
@Component
public class SeckillConsumer {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ISeckillOrderService seckillOrderService;

    @JmsListener(destination = "seckill.order")
    public void receiveQueue(Order order) {
        //保存秒杀商品订单
        orderMapper.insert(order);
    }

    @JmsListener(destination = "seckill.seckillOrder")
    public void receiveQueue2(SeckillOrder seckillOrder) {
        //保存秒杀商品记录
        seckillOrderService.save(seckillOrder);
    }
}
