package com.zbw.big.study.es.connectionpool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class EsConnectionPoolFactory implements PooledObjectFactory<RestHighLevelClient> {

	@Override
	public void activateObject(PooledObject<RestHighLevelClient> arg0) throws Exception {
		System.out.println("activateObject");
	}

	@Override
	public void destroyObject(PooledObject<RestHighLevelClient> pooledObject) throws Exception {
		RestHighLevelClient highLevelClient = pooledObject.getObject();
		highLevelClient.close();
	}

	@Override
	public PooledObject<RestHighLevelClient> makeObject() throws Exception {
		RestHighLevelClient client = null;
		try {
			client = new RestHighLevelClient(RestClient.builder(
					new HttpHost("localhost", 9200, "http"),
					new HttpHost("localhost", 9201, "http")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new DefaultPooledObject<RestHighLevelClient>(client);
	}

	@Override
	public void passivateObject(PooledObject<RestHighLevelClient> arg0) throws Exception {
		System.out.println("passivateObject");
	}

	@Override
	public boolean validateObject(PooledObject<RestHighLevelClient> arg0) {
		return true;
	}
}
