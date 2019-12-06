package com.zbw.big.study.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import com.zbw.big.study.oracle.model.TtRoBalancedJoinLabour;
import com.zbw.big.study.serviceImpl.EsIndexServiceImpl;

public class MyBatisStreamResultHander implements ResultHandler {
	private final static int BATCH_SIZE = 1000;
	private int size;
	// 存储每批数据的临时容器
	private List<TtRoBalancedJoinLabour> list = new ArrayList<TtRoBalancedJoinLabour>();;
	private TtRoBalancedJoinLabour ttRoBalancedJoinLabour;
	private EsIndexServiceImpl esIndexServiceImpl = new EsIndexServiceImpl();;
	
	public void handleResult(ResultContext resultContext) {
		ttRoBalancedJoinLabour = (TtRoBalancedJoinLabour) resultContext.getResultObject();
        System.out.println(ttRoBalancedJoinLabour);
        list.add(ttRoBalancedJoinLabour);
        size++;
        if (size == 100) {
            handle();
        }
	}
	
	private void handle() {
		System.out.println("begin index: " + new Date());
		esIndexServiceImpl.bulkIndex("balancejoinlabourpoc", list);
		System.out.println("end index: " + new Date());
		
		size = 0;
		list.clear();
	}
	
	public void end(){
		handle(); //处理最后一批不到BATCH_SIZE的数据
	}
}
