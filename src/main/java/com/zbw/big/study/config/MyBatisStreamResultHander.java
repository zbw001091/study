package com.zbw.big.study.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import com.zbw.big.study.oracle.model.TtRoBalancedJoinLabour;
import com.zbw.big.study.serviceImpl.EsIndexServiceImpl;

public class MyBatisStreamResultHander implements ResultHandler {
	private final static int BATCH_SIZE = 2000;
	private int size;
	private int batchCount = 0;
	// 存储每批数据的临时容器
	private List<TtRoBalancedJoinLabour> list = new ArrayList<TtRoBalancedJoinLabour>();;
	private TtRoBalancedJoinLabour ttRoBalancedJoinLabour;
	private EsIndexServiceImpl esIndexServiceImpl = new EsIndexServiceImpl();
	
	public void handleResult(ResultContext resultContext) {
		ttRoBalancedJoinLabour = (TtRoBalancedJoinLabour) resultContext.getResultObject();
//        System.out.println(ttRoBalancedJoinLabour);
        list.add(ttRoBalancedJoinLabour);
        size++;
        if (size == BATCH_SIZE) {
            handle();
        }
	}
	
	private void handle() {
		System.out.println("processed rowCount: " + (++batchCount)*BATCH_SIZE);
//		System.out.println("begin index: " + new Date());
		esIndexServiceImpl.bulkIndex("balancejoinlabourpoc2", list);
		System.out.println("end index2es: " + new Date());
		
		size = 0;
		list.clear();
	}
	
	public void end(){
		handle(); //处理最后一批不到BATCH_SIZE的数据
	}
}
