package com.zbw.big.study.controller;

import java.io.IOException;

import org.apache.rocketmq.client.producer.SendResult;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sgm.dmsii.message.base.MessageExtConst;
import com.sgm.dmsii.message.core.RocketMQTemplate;
import com.zbw.big.study.es.connectionpool.EsConnectionPoolUtil;
import com.zbw.big.study.es.dao.BankAccount;
import com.zbw.big.study.util.JsonUtil;

@Controller
public class ESController {
	
//	@Autowired
    private RestHighLevelClient rhlClient;
	
	@Autowired
    private RocketMQTemplate rocketMQTemplate;
	
	private String index = "splitbank"; // 从多个index中查询
	private String type = "_doc";
	
	// index单个document
	@RequestMapping("/indexOneDocument")
	public String indexOneDocument() {
		IndexRequest indexRequest = new IndexRequest(index);
		BankAccount bankAccount = new BankAccount();
		bankAccount.setAccount_number(1001);
		bankAccount.setAddress("xingzhi rd");
		bankAccount.setAge(38);
		bankAccount.setBalance(10000000);
		bankAccount.setCity("Shanghai");
        String source = JsonUtil.obj2String(bankAccount);
        indexRequest.source(source, XContentType.JSON);
        System.out.println(source);
        
        // 同步写ES
        try {
        	// 从连接池领用
        	rhlClient = EsConnectionPoolUtil.getClient();
        	
            IndexResponse indexResponse = rhlClient.index(indexRequest, RequestOptions.DEFAULT);
            
            // 记录ES的index response
            String index = indexResponse.getIndex();
            String id = indexResponse.getId();
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                System.out.println(">>> index success, _id: " + indexResponse.getId());
            } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                
            }
            ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                
            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    String reason = failure.reason();
                }
            }
            
            // 还给连接池
            EsConnectionPoolUtil.returnClient(rhlClient);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
			e.printStackTrace();
		}
        
        // 异步写ES，先写RocketMQ
        SendResult result=rocketMQTemplate.syncSend(
                "estest-es-topic:concurrently",MessageBuilder //demo_topic:concurrently ,topic=demo_topic\tag=concurrently
                        .withPayload(source)
                        .setHeader(MessageExtConst.PROPERTY_KEYS, "KEY_") //+ demo.getId())
                        .build());
        
		return "success";
	}
	
	// index by bulk
	@RequestMapping("/indexBulk")
	public String indexBulk() {
		return "success";
	}
	
	@RequestMapping("/searchOneDocument")
	public String searchOneDocument() {
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(10);
        sourceBuilder.fetchSource("", "");
        MatchQueryBuilder matchQuery1 = QueryBuilders.matchQuery("address", "xingzhi");
        MatchQueryBuilder matchQuery2 = QueryBuilders.matchQuery("city", "ID");
//        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("tag", "体育");
//        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishTime");
//        rangeQueryBuilder.gte("2018-01-26T08:00:00Z");
//        rangeQueryBuilder.lte("2018-01-26T20:00:00Z");
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(matchQuery1);
        boolQuery.mustNot(matchQuery2);
//        boolBuilder.must(termQueryBuilder);
//        boolBuilder.must(rangeQueryBuilder);
        sourceBuilder.query(boolQuery);
        SearchRequest searchRequest = new SearchRequest(index);
//        searchRequest.types(type);
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
		return "success";
	}
}
