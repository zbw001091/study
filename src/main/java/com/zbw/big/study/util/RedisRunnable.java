package com.zbw.big.study.util;

public class RedisRunnable implements Runnable {

	private RedisUtil redis;
	
    public RedisRunnable(RedisUtil redis) {
        this.redis = redis;
    }
    
	@Override
	public void run() {
		redis.hashSet();
	}

}
