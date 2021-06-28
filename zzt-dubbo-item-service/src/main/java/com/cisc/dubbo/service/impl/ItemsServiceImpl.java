package com.cisc.dubbo.service.impl;

import com.cisc.dubbo.mapper.ItemsMapper;
import com.cisc.dubbo.pojo.Items;
import com.cisc.dubbo.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("itemsService")
public class ItemsServiceImpl implements ItemsService {
	
	@Autowired
	private ItemsMapper itemsMapper;

	@Override
	public Items getItem(String item_id) {
		return itemsMapper.selectByPrimaryKey(item_id);
	}
	
	@Override
	public int getItemCounts(String item_id) {
		Items item = itemsMapper.selectByPrimaryKey(item_id);
		return item.getCounts();
	}

	@Override
	public void displayReduceCounts(String item_id, int buyCounts) {
		
//		int a  = 1 / 0;
		
		Items reduceItem = new Items();
		reduceItem.setId(item_id);
		reduceItem.setBuycounts(buyCounts);
		itemsMapper.reduceCounts(reduceItem);
	}

}

