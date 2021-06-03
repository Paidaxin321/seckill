package com.yang.seckillsys.activemq;

import com.yang.seckillsys.pojo.Order;
import com.yang.seckillsys.pojo.SeckillOrder;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;

/* 生产者 */
@Service
public class SeckillProducer {
    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;

    //异步保存普通订单
    public void sendMessage(Destination destination, final Order order) {
        jmsMessagingTemplate.convertAndSend(destination, order);
    }

    //异步保存秒杀订单
    public void sendMessage2(Destination destination, final SeckillOrder seckillOrder) {
        jmsMessagingTemplate.convertAndSend(destination, seckillOrder);
    }

}
