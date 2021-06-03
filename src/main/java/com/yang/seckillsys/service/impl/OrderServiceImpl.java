package com.yang.seckillsys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yang.seckillsys.activemq.SeckillProducer;
import com.yang.seckillsys.pojo.Order;
import com.yang.seckillsys.mapper.OrderMapper;
import com.yang.seckillsys.pojo.SeckillGoods;
import com.yang.seckillsys.pojo.SeckillOrder;
import com.yang.seckillsys.pojo.User;
import com.yang.seckillsys.service.IGoodsService;
import com.yang.seckillsys.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.seckillsys.service.ISeckillGoodsService;
import com.yang.seckillsys.service.ISeckillOrderService;
import com.yang.seckillsys.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YANGLIYUAN
 * @since 2021-04-17
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private RedisTemplate redisTemplate;

    //注入 SeckillProducer 生产者类
    @Resource
    private SeckillProducer seckillProducer;
    //定义消息队列
    private static Destination destinationOrder = new ActiveMQQueue("seckill.order");
    private static Destination destinationSeckillOrder = new ActiveMQQueue("seckill.seckillOrder");

    @Override
    public Order seckill(User user, GoodsVo goods) {
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new
                QueryWrapper<SeckillGoods>().eq("goods_id",
                goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        //秒杀商品表减库存
        boolean update_stock = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count="+"stock_count-1" ).eq("goods_id", goods.getId()).gt("stock_count", 0));
        if (!update_stock){
            return null;
        }

/*        seckillGoodsService.updateById(seckillGoods);*/
        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        //orderMapper.insert(order);
        seckillProducer.sendMessage(destinationOrder,order);
        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(goods.getId());
        //seckillOrderService.save(seckillOrder);
        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goods.getId(),seckillOrder);
        seckillProducer.sendMessage2(destinationSeckillOrder,seckillOrder);
        return order;
    }
}
