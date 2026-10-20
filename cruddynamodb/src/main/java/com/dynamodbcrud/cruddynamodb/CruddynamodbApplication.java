package com.dynamodbcrud.cruddynamodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.dynamodbcrud.cruddynamodb.repository"})
public class CruddynamodbApplication {

	public static void main(String[] args) {
		SpringApplication.run(CruddynamodbApplication.class, args);
	}

}
