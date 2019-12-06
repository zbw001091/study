package com.zbw.big.study.serviceImpl;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbw.big.study.config.MyBatisStreamResultHander;
import com.zbw.big.study.oracle.model.TtRoBalanced;
import com.zbw.big.study.oracle.model.TtRoBalancedJoinLabour;
import com.zbw.big.study.oracle.repository.TtRoBalancedMapper;
import com.zbw.big.study.service.TtRoBalancedService;

@Service
public class TtRoBalancedServiceImpl implements TtRoBalancedService {
	
	@Resource
	private TtRoBalancedMapper ttRoBalancedMapper;
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Override
	public List<TtRoBalanced> getBySingle() {
		return ttRoBalancedMapper.selectBySingle();
	}
	
	@Override
	public List<TtRoBalancedJoinLabour> getByJoin() {
		return ttRoBalancedMapper.selectByJoin();
	}
	
	@Override
	public void getByJoinWithStream() {
		MyBatisStreamResultHander myBatisStreamResultHander = new MyBatisStreamResultHander();
		sqlSession.select("com.zbw.big.study.oracle.repository.TtRoBalancedMapper.selectByJoinWithStream",myBatisStreamResultHander);
		myBatisStreamResultHander.end();
	}
}
