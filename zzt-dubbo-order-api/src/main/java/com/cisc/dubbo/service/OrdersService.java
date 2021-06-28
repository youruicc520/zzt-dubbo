package com.cisc.dubbo.service;

import com.cisc.dubbo.pojo.Orders;

public interface OrdersService {

	/**
	 * @Description: 根据订单id查询订单
	 */
	public Orders getOrder(String orderId);
	
	/**
	 * @Description: 下订单
	 */
	public boolean createOrder(String item_id);

}

