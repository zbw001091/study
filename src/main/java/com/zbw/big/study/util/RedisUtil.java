package com.zbw.big.study.util;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	private HashOperations hashOperation;
	
	public void stringSet() {
		
	}
	
	public void stringGet() {
		
	}
	
	public void hashSet() {
		hashOperation = stringRedisTemplate.opsForHash();
		hashOperation.putIfAbsent("sj0000", "key1", 1);
		stringRedisTemplate.expire("sj0000", 30000, TimeUnit.SECONDS);
	}
	
	public void hashGet() {
		hashOperation = stringRedisTemplate.opsForHash();
		Map<String, Integer> hashEntries = hashOperation.entries("sj0000");
		for(Map.Entry<String, Integer> entry : hashEntries.entrySet()){
		    String hashKey = entry.getKey();
		    int hashValue = entry.getValue();
		    System.out.println(hashKey+":"+hashValue);
		}
	}
	
	public void getSerializer() {
		/*查看redisTemplate的Serializer*/
	    System.out.println("redisTemplate.getKeySerializer(): " + stringRedisTemplate.getKeySerializer());
	    System.out.println("redisTemplate.getValueSerializer(): " + stringRedisTemplate.getValueSerializer());
	}
}
