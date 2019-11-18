package com.zbw.big.study.serviceImpl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.zbw.big.study.service.AsyncService;

@Service
public class AsyncServiceImpl implements AsyncService {

	@Override
	@Async("asyncServiceExecutor")
	public void executeAsync() {
		System.err.println("start executeAsync");
        try{
            Thread.sleep(1000);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.err.println("end executeAsync");
	}

}
