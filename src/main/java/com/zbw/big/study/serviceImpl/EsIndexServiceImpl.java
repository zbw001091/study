package com.zbw.big.study.serviceImpl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import com.zbw.big.study.es.connectionpool.EsConnectionPoolUtil;
import com.zbw.big.study.oracle.model.TtRoBalancedJoinLabour;
import com.zbw.big.study.service.EsIndexService;
import com.zbw.big.study.util.JsonUtil;

public class EsIndexServiceImpl implements EsIndexService {

	private RestHighLevelClient rhlClient;
	
	@Override
	public void index(String indexName, Object T) {
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

	@Override
	public void bulkIndex(String indexName, List<TtRoBalancedJoinLabour> list) {
		BulkRequest bulkRequest = new BulkRequest();
		IndexRequest indexRequest = null;
//		System.out.println(list.size());
		for (Object object : list) {
			indexRequest = new IndexRequest(indexName).source(JsonUtil.obj2String(object), XContentType.JSON);
			
			// 按照document的brand字段值，进行routing，不同brand的document进入该index的不同shard分片
//			indexRequest = new IndexRequest(indexName).source(JsonUtil.obj2String(object), XContentType.JSON).routing(((TtRoBalancedJoinLabour)object).getBrand());
			bulkRequest.add(indexRequest);
		}
		
		// 同步写ES
        try {
        	// 从连接池领用
        	rhlClient = EsConnectionPoolUtil.getClient();
        	System.out.println("start index2es: " + new Date());
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
}
