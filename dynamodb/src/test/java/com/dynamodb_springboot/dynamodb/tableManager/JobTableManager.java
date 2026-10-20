package com.dynamodb_springboot.dynamodb.tableManager;

import com.dynamodb_springboot.dynamodb.model.job.JobModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import static com.dynamodb_springboot.dynamodb.config.DynamoDbConfigTest.resolveTableName;

@TestComponent
@Slf4j
public class JobTableManager extends AbstractTableManager<JobModel> {
    private final DynamoDbClient dynamoDbClient;

    public JobTableManager(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    @Autowired
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Override
    public void createTable(String tableName) {
        try{
            dynamoDbClient.createTable(CreateTableRequest.builder()
                    .tableName(tableName)
                    .keySchema(KeySchemaElement.builder()
                            .attributeName("team_id")
                            .keyType(KeyType.HASH)
                            .build())
                    .attributeDefinitions(AttributeDefinition.builder()
                            .attributeName("team_id")
                            .attributeType(ScalarAttributeType.S)
                            .build())
                    .provisionedThroughput(ProvisionedThroughput.builder()
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                    .build());
            log.info("Table created: {}", tableName);
        }catch (com.amazonaws.services.dynamodbv2.model.ResourceInUseException e){
            log.warn("Table '{}' already exist.", tableName);
        }catch (Exception e) {
            log.error("Failed to create table '{}': {}", tableName, e.getMessage(), e);
        }
    }

    @Override
    public JobModel seedTable(JobModel items) {

        DynamoDbTable<JobModel> table = dynamoDbEnhancedClient.table(
                                            resolveTableName("job"),
                                            TableSchema.fromBean(JobModel.class));
        //TODO: insert items
        table.putItem(items);
        return items;
    }
}
