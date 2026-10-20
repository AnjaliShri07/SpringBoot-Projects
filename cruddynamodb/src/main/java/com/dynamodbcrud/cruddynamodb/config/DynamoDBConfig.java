package com.dynamodbcrud.cruddynamodb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
public class DynamoDBConfig {

   /* @Value("${aws.dynamodb.endpoint}")
    private String endpoint;

    @Value("${aws.dynamodb.access-key}")
    private String accessKey;

    @Value("${aws.dynamodb.secret-key}")
    private String secretKey;*/

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create("https://dynamodb.us-east-1.amazonaws.com")) // For LocalStack or Local DynamoDB
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials
                                .create("AKIAQ4J5X53DBQIAIPJS",
                                        "i4jWPk5oqPDZJEL1pUPLf2X/9iw/W9wBYI7UO7W0")))
                .build();
    }

}
