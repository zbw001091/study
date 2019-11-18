package com.zbw.big.study.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zbw.big.study.mongo.dao.MongoUser;

@Controller
public class MongoController {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@RequestMapping("/mongoFindUsers")
	public void findUsers() {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is("zbw"));
		List<MongoUser> list = mongoTemplate.find(query, MongoUser.class);
		for (MongoUser mongoUser : list) {
			System.out.println(mongoUser.getName());
		}
	}
}
