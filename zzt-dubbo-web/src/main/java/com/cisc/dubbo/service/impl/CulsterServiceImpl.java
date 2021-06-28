package com.cisc.dubbo.service.impl;

import com.cisc.dubbo.curator.DistributedLock;
import com.cisc.dubbo.service.CulsterService;
import com.cisc.dubbo.service.ItemsService;
import com.cisc.dubbo.service.OrdersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("buyService")
public class CulsterServiceImpl implements CulsterService {
	
	final static Logger log = LoggerFactory.getLogger(CulsterServiceImpl.class);
	
	@Autowired
	private ItemsService itemService;

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private DistributedLock distributedLock;
	@Override
	public void doBuyItem(String item_id) {
		// 减少库存
		itemService.displayReduceCounts(item_id, 1);
		
		// 创建订单
		ordersService.createOrder(item_id);
	}
	
	@Override
	public boolean displayBuy(String item_id) {

		//执行订单流程之前使得当前业务获得分布式锁
		distributedLock.getLock();

		int buyCounts = 6;
		
		// 1. 判断库存
		int stockCounts = itemService.getItemCounts(item_id);
		if (stockCounts < buyCounts) {
			log.info("库存剩余{}件，用户需求量{}件，库存不足，订单创建失败...", 
					stockCounts, buyCounts);
			//释放锁
			distributedLock.releaseLock();
			return false;
		}
		
		// 2. 创建订单
		boolean isOrderCreated = ordersService.createOrder(item_id);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			distributedLock.releaseLock();
		}


		// 3. 创建订单成功后，扣除库存
		if (isOrderCreated) {
			log.info("订单创建成功...");
			itemService.displayReduceCounts(item_id, buyCounts);
		} else {
			log.info("订单创建失败...");
			distributedLock.releaseLock();
			return false;
		}

		//释放锁,让下一个请求获得锁
		distributedLock.releaseLock();
		return true;
	}

}

