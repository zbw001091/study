package com.zbw.big.study.controller;

import java.io.IOException;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zbw.big.study.es.dao.BankAccount;
import com.zbw.big.study.util.JsonUtil;

@Controller
public class ESController {
	
	@Autowired
    private RestHighLevelClient rhlClient;
	
	private String index = "bank";
	private String type = "_doc";
	
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
        try {
            rhlClient.index(indexRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
		return "success";
	}
	
	@RequestMapping("/searchOneDocument")
	public String searchOneDocument() {
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(10);
        sourceBuilder.fetchSource("", "");
        MatchQueryBuilder matchQuery1 = QueryBuilders.matchQuery("age", "40");
        MatchQueryBuilder matchQuery2 = QueryBuilders.matchQuery("state", "ID");
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
            SearchResponse response = rhlClient.search(searchRequest);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
		return "success";
	}
}
