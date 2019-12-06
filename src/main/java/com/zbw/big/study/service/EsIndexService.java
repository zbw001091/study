package com.zbw.big.study.service;

import java.util.List;

import com.zbw.big.study.oracle.model.TtRoBalancedJoinLabour;

public interface EsIndexService {

	void index(String indexName, Object T);
	
	void bulkIndex(String indexName, List<TtRoBalancedJoinLabour> list);
}
