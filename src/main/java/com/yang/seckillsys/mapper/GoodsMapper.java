package com.yang.seckillsys.mapper;

import com.yang.seckillsys.pojo.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yang.seckillsys.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author YANGLIYUAN
 * @since 2021-04-17
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    //获取商品列表
    List<GoodsVo> findGoodsVo();

    //获取商品详情
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
