package com.zbw.big.study.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zbw.big.study.dao.NestedObject;
import com.zbw.big.study.dao.User;
import com.zbw.big.study.es.connectionpool.EsConnectionPoolUtil;
import com.zbw.big.study.es.dao.BankAccount;
import com.zbw.big.study.oracle.model.TtRoBalancedJoinLabour;
import com.zbw.big.study.service.BalanceService;
import com.zbw.big.study.service.TtRoBalancedService;
import com.zbw.big.study.util.JsonUtil;

@Controller
public class ESController {
	
//	@Autowired
    private RestHighLevelClient rhlClient;
	
	@Autowired
    private RocketMQTemplate rocketMQTemplate;
	
	@Autowired
	private BalanceService balanceService;
	
	@Autowired
	private TtRoBalancedService ttRoBalancedService;
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	private String index = "splitbank"; // 从多个index中查询
	
	// index单个document
	@RequestMapping("/indexOneDocument")
	public String indexOneDocument() {
		BankAccount bankAccount = new BankAccount();
		bankAccount.setAccount_number(1001);
		bankAccount.setAddress("shenjiang rd");
		bankAccount.setAge(38);
		bankAccount.setBalance(10000000);
		bankAccount.setCity("Shanghai");
		this.index("test", bankAccount);
        
		return "success";
	}
	
	// index by bulk
	// 从MySQL查出list，bulk写入ES
	@RequestMapping("/indexBulk")
	public String indexBulk() {
//		BankAccount bankAccount1 = new BankAccount();
//		bankAccount1.setAccount_number(1005);
//		bankAccount1.setAddress("xingzhi rd1");
//		bankAccount1.setAge(38);
//		bankAccount1.setBalance(10000005);
//		bankAccount1.setCity("Shanghai");
//		
//		BankAccount bankAccount2 = new BankAccount();
//		bankAccount2.setAccount_number(1006);
//		bankAccount2.setAddress("xingzhi rd2");
//		bankAccount2.setAge(38);
//		bankAccount2.setBalance(10000006);
//		bankAccount2.setCity("Shanghai");
//		
//		List<BankAccount> list = new ArrayList<BankAccount>();
//		list.add(bankAccount1);
//		list.add(bankAccount2);
		
		// 从MySQL里查询多表Join售后结算单，笛卡尔积打平，bulk进入ES
//		List<Balance> list = balanceService.getByJoin();
//		this.bulkIndex("splitbank", list);
		
		List<TtRoBalancedJoinLabour> list = ttRoBalancedService.getByJoin();
		this.bulkIndex("balancejoinlabourpoc", list);
		
		return "success";
	}
	
	@RequestMapping("/indexBulkByStream")
	public String indexBulkByStream() {
//		TtRoBalancedMapper ttRoBalancedMapper = sqlSession.getMapper(TtRoBalancedMapper.class);
//		ttRoBalancedMapper.selectByJoin();
		
		ttRoBalancedService.getByJoinWithStream();
		
//		Cursor<TtRoBalancedJoinLabour> ttRoBalancedJoinLabourCursor = sqlSession.selectCursor("com.zbw.big.study.oracle.repository.TtRoBalancedMapper.selectByJoin");
//		System.err.println(ttRoBalancedJoinLabourCursor.isOpen());
//		Iterator<TtRoBalancedJoinLabour> iter = ttRoBalancedJoinLabourCursor.iterator();
//	    List list = new ArrayList<TtRoBalancedJoinLabour>();
//	    int i = 0;
//	    System.err.println(iter.hasNext());
//	    while (iter.hasNext()) {
//	    	if (i < 3000) {
//	    		list.add(iter.next());
//	    		i++;
//	    		System.err.println("sss");
//	    	}
//	    }
	    return "success";
	}
	
	// index by bulk
	@RequestMapping("/indexNestedDoc")
	public String indexNestedDoc() {
		NestedObject nestedObject = new NestedObject();
		User user1 = new User();
		user1.setUserId("fan");
		user1.setUserName("zhiyi");
		User user2 = new User();
		user2.setUserId("xu");
		user2.setUserName("genbao");
		List<User> users = new ArrayList<User>();
		users.add(user1);
		users.add(user2);
		nestedObject.setGroup("shanghai shenhua");
		nestedObject.setUserlist(users);
		this.index("nested", nestedObject);
		
		return "success";
	}
	
	@RequestMapping("/searchOneDocument")
	public String searchOneDocument() {
		this.searchDocument();
		return "success";
	}
	
	/**
	 * 
	 * @param indexName indexName
	 * @param T object to be indexed
	 */
	private void index(String indexName, Object T) {
		IndexRequest indexRequest = new IndexRequest(indexName);
        String source = JsonUtil.obj2String(T);
        indexRequest.source(source, XContentType.JSON);
        System.out.println(source);
        
        // 同步写ES
        try {
        	// 从连接池领用
        	rhlClient = EsConnectionPoolUtil.getClient();
        	
            IndexResponse indexResponse = rhlClient.index(indexRequest, RequestOptions.DEFAULT);
            
            // 记录ES的index response
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
//        SendResult result=rocketMQTemplate.syncSend(
//                "estest-es-topic:concurrently",MessageBuilder //demo_topic:concurrently ,topic=demo_topic\tag=concurrently
//                        .withPayload(source)
//                        .setHeader(MessageExtConst.PROPERTY_KEYS, "KEY_") //+ demo.getId())
//                        .build());
	}
	
	/**
	 * 
	 * @param indexName indexName
	 * @param list: list of object to be bulk indexed
	 */
	private void bulkIndex(String indexName, List<TtRoBalancedJoinLabour> list) {
		BulkRequest bulkRequest = new BulkRequest();
		IndexRequest indexRequest = null;
		System.out.println(list.size());
		for (Object object : list) {
			indexRequest = new IndexRequest(indexName).source(JsonUtil.obj2String(object), XContentType.JSON);
			bulkRequest.add(indexRequest);
		}
		
		// 同步写ES
        try {
        	// 从连接池领用
        	rhlClient = EsConnectionPoolUtil.getClient();
        	
        	BulkResponse bulkResponse = rhlClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        	
        	if (bulkResponse.hasFailures()) {
        		for (BulkItemResponse bulkItemResponse : bulkResponse) {
        		    if (bulkItemResponse.isFailed()) {
        		        BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
        		        System.out.println(failure.getMessage());
        		        System.out.println(failure.getType());
        		        System.out.println(failure.getCause());
        		    }
        		}
        	}
        	
        	for (BulkItemResponse bulkItemResponse : bulkResponse) {
        	    DocWriteResponse itemResponse = bulkItemResponse.getResponse();
        	    
        	    switch (bulkItemResponse.getOpType()) {
        	    case INDEX:
        	    case CREATE:
        	        IndexResponse indexResponse = (IndexResponse) itemResponse;
//        	        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
//                        System.out.println(">>> index success, _id: " + indexResponse.getId());
//                    }
        	        break;
        	    case UPDATE:
        	        UpdateResponse updateResponse = (UpdateResponse) itemResponse;
        	        break;
        	    case DELETE:
        	        DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
        	    }
        	}
        	
        	// 还给连接池
            EsConnectionPoolUtil.returnClient(rhlClient);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void searchDocument() {
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(10);
        String[] includes = new String[] { "balanceNo","roNo","serviceAdvisor","repairItemId" };
        sourceBuilder.fetchSource(includes, null);
        
        /**
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
        **/
        
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        sourceBuilder.query(matchAllQueryBuilder);
        
        SearchRequest searchRequest = new SearchRequest("balancepoc");
//        searchRequest.types(type);
        searchRequest.source(sourceBuilder);
        try {
        	// 从连接池领用
        	rhlClient = EsConnectionPoolUtil.getClient();
        	
            SearchResponse response = rhlClient.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(response);
            
            // 还给连接池
            EsConnectionPoolUtil.returnClient(rhlClient);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
			e.printStackTrace();
		}
	}
}
