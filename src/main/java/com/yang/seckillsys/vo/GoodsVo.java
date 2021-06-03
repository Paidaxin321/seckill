package com.yang.seckillsys.vo;

import com.yang.seckillsys.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor      //无参构造
@AllArgsConstructor     //有参构造
public class GoodsVo extends Goods {

    private BigDecimal seckillPrice;        //秒杀价

    private Integer stockCount;             //库存数量

    private Date startDate;                 //秒杀开始时间

    private Date endDate;                   //秒杀结束时间
}
