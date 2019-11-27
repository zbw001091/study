package com.zbw.big.study;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sgm.dmsii.message.annotation.EnableMQConfiguration;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableMQConfiguration
@MapperScan("com.zbw.big.study.repository")
public class App 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
    }
}
