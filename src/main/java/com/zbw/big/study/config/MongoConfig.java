package com.zbw.big.study.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
public class MongoConfig {

	@Autowired
	private MongoSettingsProperties mongoSettingsProperties;
	
	@Bean
	public MongoDbFactory mongoDbFactory() {
		MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.connectionsPerHost(mongoSettingsProperties.getMaxConnectionsPerHost());
        builder.minConnectionsPerHost(mongoSettingsProperties.getMinConnectionsPerHost());
        if (mongoSettingsProperties.getReplicaSet() != null) {
            builder.requiredReplicaSetName(mongoSettingsProperties.getReplicaSet());
        }
        MongoClientOptions mongoClientOptions = builder.build();
        
        // MongoDB地址列表
        List<ServerAddress> serverAddresses = new ArrayList<>();
        for (String host : mongoSettingsProperties.getAddress()) {
            Integer index = mongoSettingsProperties.getAddress().indexOf(host);
            Integer port = mongoSettingsProperties.getPort().get(index);
            ServerAddress serverAddress = new ServerAddress(host, port);
            serverAddresses.add(serverAddress);
        }
        System.out.println("serverAddresses:" + serverAddresses.toString());
        
        // 连接认证
        List<MongoCredential> mongoCredentialList = new ArrayList<>();
        if (mongoSettingsProperties.getUsername() != null) {
            mongoCredentialList.add(MongoCredential.createScramSha1Credential(
					mongoSettingsProperties.getUsername(),
                    mongoSettingsProperties.getAuthenticationDatabase() != null ? mongoSettingsProperties.getAuthenticationDatabase() : mongoSettingsProperties.getDatabase(),
                    mongoSettingsProperties.getPassword().toCharArray()));
        }
        System.out.println("mongoCredentialList:" + mongoCredentialList.toString());
        
        //创建客户端和Factory
        MongoClient mongoClient = new MongoClient(serverAddresses, mongoCredentialList, mongoClientOptions);
        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClient, mongoSettingsProperties.getDatabase());
        
        return mongoDbFactory;
	}
}
