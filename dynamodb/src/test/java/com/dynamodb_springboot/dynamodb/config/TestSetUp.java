package com.dynamodb_springboot.dynamodb.config;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.dynamodb_springboot.dynamodb.repository.JobRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Import(DynamoDbConfigTest.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class TestSetUp {

    protected List<Object> tableData;

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Autowired
    private DynamoDbTableInitializer tableManager;

    private static DynamoDBProxyServer dynamoDBProxyServer;

    @Autowired
    private JobRepository jobRepository;

    @BeforeAll
    void startDynamoDbLocalServerAndTableCreation() throws Exception {
        String[] args ={"-inMemory", "-port", "8080"};
        dynamoDBProxyServer = ServerRunner.createServerFromCommandLineArgs(args);
        dynamoDBProxyServer.start();
        log.info("DynamoDb Local server on port {}", args[2]);
        tableManager.createAllTables();
        tableData = tableManager.seedAllTable();
    }

    @AfterAll
    void stopDynamoDbLocalServerAndDeleteTable() throws Exception {
        tableManager.deleteAllTables();
        if(dynamoDBProxyServer != null){
            dynamoDBProxyServer.stop();
            log.info("DynamoDb Local server stopped");
        }
    }

    @Test
    void testDynamoDbConnection() {
        assertNotNull(dynamoDbClient, "DynamoDbClient initiated");
    }

    @Test
    void checkDynamoDbEndpoint() {
        log.info("DynamoDbClient endpoint: {}", dynamoDbClient.serviceClientConfiguration().endpointOverride());
    }

    @Test
    void listDynamoDbTables() {
        log.info("DynamoDb tables: {}", dynamoDbClient.listTables().tableNames());
    }
}
