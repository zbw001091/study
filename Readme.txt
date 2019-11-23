localhost:8083/study/asyncTask

【关于连接池】
Redis连接池：使用application.yml的配置，采用约定大于配置，我没有@Configuration配置单独的连接池bean，这由springboot框架自动帮我建连接池bean。
Mongo连接池：基于application.yml的配置，我手工@Configuration配置单独的连接池bean（MongoDbFactory），这个bean会被MongoTemplate自动注入使用。
MySQL Datasource连接池：基于application.yml的配置，我手工@Configuration配置单独的连接池bean（DruidDataSource），这个bean会被mybatis自动注入使用。

【SpringBoot与线程池】
ExecutorConfig注册自定义线程池bean。
在Service方法中用@Async注解，表示service方法以task形式扔进线程池queue。
