package com.dynamodb_springboot.dynamodb.model.job;

import com.dynamodb_springboot.dynamodb.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
@Setter
public class JobModel extends BaseModel {

    @JsonProperty("team_id")
    @NotBlank(message = "Team ID cannot be blank")
    private String teamId;

    @JsonProperty("job_id#version")
    @NotBlank(message = "Job ID and version cannot be blank")
    private String jobIdAndVersion;

    @JsonProperty("job_id")
    @NotBlank(message = "Job ID cannot be blank")
    private String jobId;

    @JsonProperty("name")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @JsonProperty("description")
    @NotBlank(message = "Description cannot be blank")
    private String description;

    @JsonProperty("config")
    @NotEmpty(message = "Config cannot be empty")
    private JobConfigModel config;

    @JsonProperty("status")
    @NotBlank(message = "Status cannot be blank")
    private String status;

    @Override
    @DynamoDbIgnore
    public String getPartitionKey(){
        return teamId;
    }

    @Override
    @DynamoDbIgnore
    public void setPartitionKey(String partitionKey){
        this.teamId = partitionKey;
    }

    @Override
    @DynamoDbIgnore
    public String getSortKey() {
        return jobIdAndVersion;
    }

    @Override
    @DynamoDbIgnore
    public void setSortKey(String sortKey) {
        this.jobIdAndVersion = sortKey;
    }

    @DynamoDbPartitionKey
    @DynamoDbSecondaryPartitionKey(indexNames = "team_id_job_id_gsi")
    @DynamoDbAttribute("team_id")
    public String getTeamId() {
        return teamId;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("job_id#version")
    public String getJobIdAndVersion() {
        return jobIdAndVersion;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "team_id_job_id_gsi")
    @DynamoDbAttribute("job_id")
    public String getJobId() {
        return jobId;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "job_name_version_gsi")
    @DynamoDbAttribute("name")
    public String getName() {
        return name;
    }

    @DynamoDbAttribute("description")
    public String getDescription() {
        return description;
    }

    @DynamoDbAttribute("config")
    public JobConfigModel getConfig() {
        return config;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "job_name_version_gsi")
    @DynamoDbAttribute("version")
    public String getVersion(){
        return version;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "status_gsi")
    @DynamoDbAttribute("status")
    public String getStatus() {
        return status;
    }
}
