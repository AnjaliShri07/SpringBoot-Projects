package com.dynamodb_springboot.dynamodb.tableManager;

import com.dynamodb_springboot.dynamodb.model.BaseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

@Slf4j
public abstract class AbstractTableManager<T extends BaseModel>{

    @Autowired
    private DynamoDbClient dynamoDbClient;

    public abstract void createTable(String tableName);

    public abstract T seedTable(T entity);

    public void deleteTable(String tableName) {
        try{
            dynamoDbClient.deleteTable(DeleteTableRequest.builder()
                    .tableName(tableName)
                    .build());
            log.info("Table '{}' deleted.", tableName);
        }catch (ResourceNotFoundException e){
            log.warn("Table '{}' doesn't exist.", tableName);
        }catch (Exception e) {
            log.error("Failed to delete table '{}': {}", tableName, e.getMessage(), e);
        }
    }

}
