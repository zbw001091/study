package com.zbw.big.study.serviceImpl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zbw.big.study.dao.Balance;
import com.zbw.big.study.repository.BalanceMapper;
import com.zbw.big.study.service.BalanceService;

@Service
public class BalanceServiceImpl implements BalanceService {

	@Resource
	private BalanceMapper balanceMapper;
	
	@Override
	public List<Balance> getByJoin() {
		return balanceMapper.selectByJoin();
	}

}
