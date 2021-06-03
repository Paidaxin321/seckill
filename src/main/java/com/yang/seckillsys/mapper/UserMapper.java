package com.yang.seckillsys.mapper;

import com.yang.seckillsys.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author YANGLIYUAN
 * @since 2021-04-12
 */

@Repository
public interface UserMapper extends BaseMapper<User> {

}
