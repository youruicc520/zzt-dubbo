package com.cisc.dubbo.service.impl;

import com.cisc.dubbo.mapper.OrdersMapper;
import com.cisc.dubbo.pojo.Orders;
import com.cisc.dubbo.service.OrdersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("ordersService")
public class OrdersServiceImpl implements OrdersService {
	
	final static Logger log = LoggerFactory.getLogger(OrdersServiceImpl.class);
	
	@Autowired
	private OrdersMapper ordersMapper;
	
	@Override
	public Orders getOrder(String orderId) {
		return ordersMapper.selectByPrimaryKey(orderId);
	}

	@Override
	public boolean createOrder(String item_id) {
		
		// 创建订单
		String oid = UUID.randomUUID().toString().replaceAll("-", "");
		Orders o = new Orders();
		o.setId(oid);
		o.setOrder_num(oid);
		o.setItem_id(item_id);
		ordersMapper.insert(o);
		
		log.info("订单创建成功");
		
		return true;
	}

}

