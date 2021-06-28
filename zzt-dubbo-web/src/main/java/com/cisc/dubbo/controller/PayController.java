package com.cisc.dubbo.controller;

import com.cisc.dubbo.curator.ZKCurator;
import com.cisc.dubbo.service.CulsterService;
import com.cisc.dubbo.utils.IMoocJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: 订购商品controller
 */
@Controller
public class PayController {
	
	@Autowired
	private CulsterService buyService;

	@Autowired
	private ZKCurator zkCurator;
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	
	@GetMapping("/buy")
	@ResponseBody
	public IMoocJSONResult doGetlogin(String item_id) {
		boolean result;
		if (StringUtils.isNotBlank(item_id)) {
			result = buyService.displayBuy(item_id);
		} else {
			return IMoocJSONResult.errorMsg("商品id不能为空");
		}
		
		return IMoocJSONResult.ok(result ? "订单创建成功..." : "订单创建失败...");
	}

	@GetMapping("/buy2")
	@ResponseBody
	public IMoocJSONResult doGetlogin2(String item_id) {

		boolean result;
		if (StringUtils.isNotBlank(item_id)) {
			result = buyService.displayBuy(item_id);
		} else {
			return IMoocJSONResult.errorMsg("商品id不能为空");
		}

		return IMoocJSONResult.ok(result ? "订单创建成功..." : "订单创建失败...");
	}

	@RequestMapping("/isZKAlive")
	@ResponseBody
	public IMoocJSONResult isZKAlive(){
		return IMoocJSONResult.ok(zkCurator.isZKAlive() ? "连接" : "断开");
	}
}
