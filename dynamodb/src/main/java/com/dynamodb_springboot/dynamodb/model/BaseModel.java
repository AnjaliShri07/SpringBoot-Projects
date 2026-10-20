package com.dynamodb_springboot.dynamodb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;

import java.io.Serializable;

@DynamoDbBean
@Setter
public abstract class BaseModel implements Serializable{

    @JsonIgnore
    private String partitionKey;

    @JsonIgnore
    private String sortKey;

    @NotBlank
    @JsonProperty("created_by")
    protected String createdBy;

    @NotBlank
    @JsonProperty("updated_by")
    protected String updatedBy;

    @NotBlank
    @JsonProperty("created_at")
    protected String createdAt;

    @NotBlank
    @JsonProperty("updated_at")
    protected String updatedAt;

    @NotBlank
    @JsonProperty("version")
    protected String version;

    @NotBlank
    @JsonProperty("is_latest_version")
    protected String isLatestVersion;

    @DynamoDbIgnore
    public String getPartitionKey() {
        return partitionKey;
    }

    @DynamoDbIgnore
    public void setPartitionKey(String partitionKey){
        this.partitionKey = partitionKey;
    }

    @DynamoDbIgnore
    public String getSortKey() {
        return sortKey;
    }

    @DynamoDbIgnore
    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    @DynamoDbAttribute("created_by")
    public String getCreatedBy() {
        return createdBy;
    }

    @DynamoDbAttribute("updated_by")
    public String getUpdatedBy() {
        return updatedBy;
    }

    @DynamoDbAttribute("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @DynamoDbAttribute("updated_at")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @DynamoDbAttribute("version")
    public String getVersion() {
        return version;
    }

    @DynamoDbAttribute("is_latest_version")
    public String getIsLatestVersion() {
        return isLatestVersion;
    }

    public BaseModel deepCopy(){
        return SerializationUtils.clone(this);
    }
}
