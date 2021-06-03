package com.yang.seckillsys.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author zhoubin
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {

	private long code;
	private String message;
	private Object obj;

	/**
	 * 功能描述: 成功返回结果
	 *
	 * @param:
	 * @return:

	 * @since: 1.0.0
	 * @Author: YANGLIYUAN
	 */
	public static RespBean success(){
		return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMessage(),null);
	}

	/**
	 * 功能描述: 成功返回结果
	 *
	 * @param:
	 * @return:
	 * @since: 1.0.0
	 * @Author: YANGLIYUAN
	 */
	public static RespBean success(Object obj){
		return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBean.success().getMessage(),obj);
	}


	/**
	 * 功能描述: 失败返回结果
	 *
	 * @param:
	 * @return:
	 *
	 * @since: 1.0.0
	 */
	public static RespBean error(RespBeanEnum respBeanEnum){
		return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMessage(),null);
	}


	/**
	 * 功能描述: 失败返回结果
	 *
	 * @param:
	 * @return:
	 */
	public static RespBean error(RespBeanEnum respBeanEnum,Object obj){
		return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMessage(),obj);
	}

}