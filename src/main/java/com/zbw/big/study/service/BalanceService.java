package com.zbw.big.study.service;

import java.util.List;

import com.zbw.big.study.dao.Balance;
import com.zbw.big.study.dao.NestedBalance;

public interface BalanceService {

	List<Balance> getByJoin();
	
	List<NestedBalance> getByNestedJoin();
}
