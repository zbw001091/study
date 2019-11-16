package com.zbw.big.study.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zbw.big.study.util.RedisRunnable;
import com.zbw.big.study.util.RedisUtil;

@Controller
public class RedisController {

	@Autowired
	private RedisUtil redis;

	@Autowired
	private RedisTemplate redisTemplate;
	
	@Resource(name = "threadPool4Redis")
	private ExecutorService threadPoolRedis;

	@RequestMapping("/saveKPI")
	public void saveKPI() {
		redis.hashSet();
	}

	@RequestMapping("/getKPI")
	public String getKPI() {
		redis.hashGet();
		return "success";
	}

	@RequestMapping("/saveKPIwithPipeline")
	public void saveKPIwithPipeline() {
        List<Object> resultList = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
//                2.connection 打开管道
//                connection.openPipeline();
            	
//                3.connection 给本次管道内添加 要一次性执行的多条命令
//                3.1 一个set操作
                byte[] key1 = "mykey1".getBytes();
                byte[] value1 = "字符串value".getBytes();
                connection.set(key1,value1);

//                3.2一个批量mset操作
                Map<byte[],byte[]> tuple = new HashMap<>();
                tuple.put("m_mykey1".getBytes(),"m_value1".getBytes());
                tuple.put("m_mykey2".getBytes(),"m_value2".getBytes());
                tuple.put("m_mykey3".getBytes(),"m_value3".getBytes());
                connection.mSet(tuple);

//                 3.3一个get操作
                System.err.println(connection.get("m_mykey2".getBytes()));

//                4.关闭管道 不需要close 否则拿不到返回值
//                connection.closePipeline();

//                这里一定要返回null，最终pipeline的执行结果，才会返回给最外层
                return null;
            }
        });
	}

	// 模拟20条线程，并发写Redis
	@RequestMapping("/saveKPIconcurrently")
	public String saveKPIconcurrently() {
		for (int i = 0; i <= 20; i++) { // 20个任务，扔进线程池，并发使用20条线程
			threadPoolRedis.execute(new RedisRunnable(redis));
		}
		System.out.println("/saveKPIconcurrently finish");
		return "success";
	}
}
