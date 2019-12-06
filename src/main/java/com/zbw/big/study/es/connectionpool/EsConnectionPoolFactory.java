package com.zbw.big.study.es.connectionpool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;

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
//		try {
//			client = new RestHighLevelClient(RestClient.builder(
//					new HttpHost("localhost", 9200, "http"),
//					new HttpHost("localhost", 9201, "http")));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return new DefaultPooledObject<RestHighLevelClient>(client);
		
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic","ehgjqQWcvFM5MKaekNbPkfsC"));
		RestClientBuilder builder = RestClient.builder(new HttpHost("fd025d4752df4290b99f1b8fde02b315.10.203.80.133.ip.es.io", 9200))
		    .setHttpClientConfigCallback(new HttpClientConfigCallback() {
		        @Override
		        public HttpAsyncClientBuilder customizeHttpClient(
		                HttpAsyncClientBuilder httpClientBuilder) {
		            return httpClientBuilder
		                .setDefaultCredentialsProvider(credentialsProvider);
		        }
		    });
		client = new RestHighLevelClient(builder);
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
