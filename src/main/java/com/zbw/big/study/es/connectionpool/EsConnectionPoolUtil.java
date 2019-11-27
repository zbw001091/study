package com.zbw.big.study.es.connectionpool;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.elasticsearch.client.RestHighLevelClient;

public class EsConnectionPoolUtil {
	// 对象池配置类
	private static GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

	// 采用默认配置maxTotal是8
	{
		poolConfig.setMaxTotal(8);
	}

	// 要池化的对象的工厂类
	private static EsConnectionPoolFactory esClientPoolFactory = new EsConnectionPoolFactory();

	// 利用对象工厂类和配置类生成对象池
	private static GenericObjectPool<RestHighLevelClient> connectionPool = new GenericObjectPool<>(esClientPoolFactory,	poolConfig);
	
	public static RestHighLevelClient getClient() throws Exception {
		RestHighLevelClient client = connectionPool.borrowObject();
		return client;
	}
	
	public static void returnClient(RestHighLevelClient client) {
		connectionPool.returnObject(client);
	}
}
