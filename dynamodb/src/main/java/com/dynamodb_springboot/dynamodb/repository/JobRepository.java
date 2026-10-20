package com.dynamodb_springboot.dynamodb.repository;

import com.dynamodb_springboot.dynamodb.model.job.JobModel;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import static com.dynamodb_springboot.dynamodb.commons.Constant.JOB;
import static com.dynamodb_springboot.dynamodb.config.DynamoDbConfig.resolveTableName;

@Repository
public class JobRepository extends BaseRepository<JobModel>{

    protected JobRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        super(dynamoDbEnhancedClient, JobModel.class, resolveTableName(JOB));
    }
}
