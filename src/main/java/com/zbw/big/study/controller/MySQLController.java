package com.zbw.big.study.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zbw.big.study.dao.Balance;
import com.zbw.big.study.dao.NestedBalance;
import com.zbw.big.study.dao.NestedLabour;
import com.zbw.big.study.dao.User;
import com.zbw.big.study.service.BalanceService;
import com.zbw.big.study.service.UserService;

@Controller
public class MySQLController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private BalanceService balanceService;
	
	@RequestMapping("/getAllUsers")
	public String getAllUsers() {
		List<User> list = userService.getAllUsers();
		System.out.println("list.size():" + list.size());
		return "success";
	}
	
	// 查询一个多表join，打平，笛卡尔积
	@RequestMapping("/getAllBalances")
	public String getAllBalances() {
		List<Balance> list = balanceService.getByJoin();
		System.out.println("list.size():" + list.size());
		for (Balance balance : list) {
			System.out.println(balance.getPartName());
		}
		return "success";
	}
	
	// 查询一个多表join，返回Nested嵌套对象（而非打平flat）
	@RequestMapping("/getAllNestedBalances")
	public String getAllNestedBalances() {
		List<NestedBalance> list = balanceService.getByNestedJoin();
		System.out.println("list.size():" + list.size());
		for (NestedBalance nestedBalance : list) {
			System.out.println(nestedBalance.getBalanceNo());
			System.out.println(nestedBalance.getLabours().size() + " labours");
			for (NestedLabour nestedLabour : nestedBalance.getLabours()) {
				System.out.println(nestedLabour.getLabourName() + " has " + nestedLabour.getParts().size() + " parts");
			}
		}
		return "success";
	}
}
