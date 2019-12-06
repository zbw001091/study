package com.zbw.big.study.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zbw.big.study.oracle.model.TtRoBalanced;
import com.zbw.big.study.oracle.model.TtRoBalancedJoinLabour;
import com.zbw.big.study.service.TtRoBalancedService;

@Controller
public class OracleController {
	
	@Autowired
	private TtRoBalancedService ttRoBalancedService;
	
	@RequestMapping("/getOneTtRoBalanced")
	public String getOneTtRoBalanced() {
		List<TtRoBalanced> list = ttRoBalancedService.getBySingle();
		System.out.println("list.size():" + list.size());
		for (TtRoBalanced ttRoBalanced : list) {
			System.out.println("ttRoBalanced.getBalanceNo():" + ttRoBalanced.getBalanceNo());
			System.out.println("ttRoBalanced.getCardNo():" + ttRoBalanced.getCardNo());
		}
		
		return "success";
	}
	
	@RequestMapping("/getOneTtRoBalancedJoinLabour")
	public String getOneTtRoBalancedJoinLabour() {
//		List<TtRoBalancedJoinLabour> list = ttRoBalancedService.getByJoin();
//		System.out.println("list.size():" + list.size());
//		for (TtRoBalancedJoinLabour ttRoBalancedJoinLabour : list) {
//			System.out.println("ttRoBalancedJoinLabour.getBalanceNo():" + ttRoBalancedJoinLabour.getBalanceNo());
//			System.out.println("ttRoBalancedJoinLabour.getCardNo():" + ttRoBalancedJoinLabour.getRepairItemId());
//		}
		return "success";
	}
}
