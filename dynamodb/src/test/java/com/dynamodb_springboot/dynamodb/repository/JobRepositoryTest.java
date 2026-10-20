package com.dynamodb_springboot.dynamodb.repository;

import com.dynamodb_springboot.dynamodb.config.TestSetUp;
import com.dynamodb_springboot.dynamodb.model.job.JobModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;


@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class JobRepositoryTest extends TestSetUp {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Autowired
    private JobRepository jobRepository;

    private JobModel seededJobData() {
        return (JobModel) tableData.stream().filter(item -> item instanceof  JobModel)
                .findFirst()
                .orElse(null);
    }


}
