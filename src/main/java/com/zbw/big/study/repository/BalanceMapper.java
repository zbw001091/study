package com.zbw.big.study.repository;

import java.util.List;

import com.zbw.big.study.dao.Balance;

public interface BalanceMapper {
    
	List<Balance> selectByJoin();
}