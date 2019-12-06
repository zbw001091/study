package com.zbw.big.study.repository;

import java.util.List;

import com.zbw.big.study.dao.Balance;
import com.zbw.big.study.dao.NestedBalance;

public interface BalanceMapper {
    
	List<Balance> selectByJoin();
	
	List<NestedBalance> selectByNestedJoin();
}