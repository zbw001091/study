package com.zbw.big.study.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadPoolRedis {

	@Bean(value = "threadPool4Redis")
	public ExecutorService createThreadPoolRedis() {
		ThreadPoolExecutor pool = new ThreadPoolExecutor(20, 30, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>());
		return pool;
	}
}
