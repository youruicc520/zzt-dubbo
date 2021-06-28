package com.cisc.dubbo.service;


import com.cisc.dubbo.pojo.Items;

public interface ItemsService {

	/**
	 * @Description: 根据商品id获取商品
	 */
	public Items getItem(String item_id);
	
	/**
	 * @Description: 查询商品库存
	 */
	public int getItemCounts(String item_id);
	
	/**
	 * @Description: 购买商品成功后减少库存
	 */
	public void displayReduceCounts(String item_id, int buyCounts);

}

