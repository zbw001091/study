package com.zbw.big.study.service;

import java.util.List;

import com.zbw.big.study.oracle.model.TtRoBalanced;
import com.zbw.big.study.oracle.model.TtRoBalancedJoinLabour;

public interface TtRoBalancedService {

	List<TtRoBalanced> getBySingle();
	
	List<TtRoBalancedJoinLabour> getByJoin();
	
	void getByJoinWithStream();
}
