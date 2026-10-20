package com.dynamodb_springboot.dynamodb.config;

import com.dynamodb_springboot.dynamodb.model.job.JobModel;
import com.dynamodb_springboot.dynamodb.tableManager.AbstractTableManager;
import com.dynamodb_springboot.dynamodb.tableManager.JobTableManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestComponent;

import java.util.ArrayList;
import java.util.List;

import static com.dynamodb_springboot.dynamodb.config.DynamoDbConfigTest.resolveTableName;

@TestComponent
@Slf4j
public class DynamoDbTableInitializer {

    private final List<AbstractTableManager> tableManagers;

    public DynamoDbTableInitializer(List<AbstractTableManager> tableManagers) {
        this.tableManagers = tableManagers;
    }

    public void createAllTables(){
        tableManagers.forEach(manager -> manager.createTable(resolveTableName("job")));
    }

    public List<Object> seedAllTable(){
        List<Object> seededItems = new ArrayList<>();
        tableManagers.forEach(manager -> {
            if(manager instanceof JobTableManager){
                JobModel jobModel = new JobModel();
                seededItems.add(manager.seedTable(jobModel));
            }
        });

        return seededItems;
    }


    public void deleteAllTables() {
        tableManagers.forEach(manager -> manager.deleteTable("job"));
    }
}
