package com.cisc.dubbo.service;

public interface CulsterService {

	/**
	 * @Description: 购买商品
	 */
	public void doBuyItem(String item_id);
	
	public boolean displayBuy(String item_id);
}

