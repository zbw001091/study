package com.zbw.big.study.config;

import java.net.UnknownHostException;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.client.RestHighLevelClient;

//@Configuration
public class ElasticSearchConfig {

//	@Bean
    public RestHighLevelClient clientWithAuth() throws UnknownHostException {
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
		
		RestHighLevelClient client = new RestHighLevelClient(builder);
		return client;
	}
		
//	@Bean
    public RestHighLevelClient client() throws UnknownHostException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")
                        ));
        return client;
	}
}
