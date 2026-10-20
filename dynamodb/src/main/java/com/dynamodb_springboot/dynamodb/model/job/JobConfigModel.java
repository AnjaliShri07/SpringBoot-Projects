package com.dynamodb_springboot.dynamodb.model.job;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.List;

@DynamoDbBean
@Setter
public class JobConfigModel {

    @JsonProperty
    private String maxRetries;

    private String frequency;

    private String scheduledTime;

    private List<String> alertRecipients;
}
