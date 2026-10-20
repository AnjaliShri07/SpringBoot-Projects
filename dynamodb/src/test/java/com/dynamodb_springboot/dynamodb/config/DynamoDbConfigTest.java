package com.dynamodb_springboot.dynamodb.config;


import com.dynamodb_springboot.dynamodb.tableManager.JobTableManager;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;
import java.util.List;

import static com.dynamodb_springboot.dynamodb.commons.Constant.HYPHEN;
import static com.dynamodb_springboot.dynamodb.commons.Constant.REGION;

@TestConfiguration
@Slf4j
public class DynamoDbConfigTest {
    @Value("${aws.dynamodb.endpoint}")
    private String endpoint;

    @Value("${aws.dynamodb.access-key}")
    private String accessKey;

    @Value("${aws.dynamodb.secret-key}")
    private String secretKey;

    @Value("${aws.dynamodb.table-prefix}")
    private String prefix;

    @Value("${aws.dynamodb.table-suffix}")
    private String suffix;

    private static String tablePrefix;
    private static String tableSuffix;

    @PostConstruct
    public void init() {
        tablePrefix = this.prefix;
        tableSuffix = this.suffix;
    }

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(REGION)
                .endpointOverride(URI.create(endpoint)) // For LocalStack or Local DynamoDB
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials
                                        .create(accessKey, secretKey)))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
    }

    @Bean
    public JobTableManager jobTableManagement(DynamoDbClient dynamoDbClient) {
        return new JobTableManager(dynamoDbClient);
    }

    @Bean
    DynamoDbTableInitializer tableManager(DynamoDbClient dynamoDbClient, JobTableManager jobTableManager) {
        return new DynamoDbTableInitializer(List.of(jobTableManager));
    }

    public static String resolveTableName(String baseTableName) {
        return tablePrefix + HYPHEN + baseTableName + HYPHEN + tableSuffix;
    }

}
