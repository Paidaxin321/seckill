package com.yang.seckillsys.service;

import com.yang.seckillsys.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.seckillsys.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YANGLIYUAN
 * @since 2021-04-17
 */
public interface IGoodsService extends IService<Goods> {
    //获取商品列表
    List<GoodsVo> findGoodsVo();

    //获取商品详情
    GoodsVo findGoodsByGoodsId(Long goodsId);

}
