package com.dynamodb_springboot.dynamodb.repository;

import com.dynamodb_springboot.dynamodb.base.ReadWriteInterface;
import com.dynamodb_springboot.dynamodb.commons.Constant.QueryOperator;
import com.dynamodb_springboot.dynamodb.model.BaseModel;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional.*;

@Slf4j
public abstract class BaseRepository<T extends BaseModel> implements ReadWriteInterface<T> {

    protected final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    protected final DynamoDbTable<T> table;

    protected BaseRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient, Class<T> clazz, String tableName) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
        this.table = dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(clazz));
    }

    @Override
    public void createItem(T entity){
        table.putItem(entity);

    }

    @Override
    public T getItem(String partitionKey, Optional<String> sortKey){
        Key key = getKey(partitionKey, sortKey);
        GetItemEnhancedRequest request = GetItemEnhancedRequest.builder().key(key).build();
        return table.getItem(request);
    }

    @Override
    public void updateItem(T entity){
        table.updateItem(entity);
    }

    @Override
    public void deleteItem(String partitionKey, Optional<String> sortKey){
        Key key = getKey(partitionKey, sortKey);
        table.deleteItem(key);
    }

    @Override
    public List<T> query(String partitionKey, Optional<String> sortKey, Optional<String> indexName,
                  Optional<List<String>> projectionAttribute, QueryOperator queryOperator,
                  Optional<String> sortKeyEnd){

        QueryConditional queryConditional = buildQueryConditional(partitionKey, sortKey, queryOperator, sortKeyEnd);

        QueryEnhancedRequest.Builder queryRequestBuilder = QueryEnhancedRequest.builder()
                                                                    .queryConditional(queryConditional);

        projectionAttribute.ifPresent(queryRequestBuilder :: attributesToProject);

        if(indexName.isPresent() && projectionAttribute.isPresent() && !projectionAttribute.get().isEmpty()){
            queryRequestBuilder.attributesToProject(projectionAttribute.get());
        }

        QueryEnhancedRequest request = queryRequestBuilder.build();

        SdkIterable<Page<T>> results = indexName
                .map(index -> {
                    log.info("Query table with index: {}", index);
                    return table.index(index).query(request);
                })
                .orElseGet(()-> {
                    return table.query(request);
                });
        return results.stream().flatMap(page -> page.items().stream())
                .toList();

    }

    @Override
    public List<T> scan(Optional<Map<String, String>> filterAttributes,
                 Optional<List<String>> projectionAttribute){

        ScanEnhancedRequest.Builder scanRequestBuilder = ScanEnhancedRequest.builder();

        filterAttributes.filter(map-> !map.isEmpty()).ifPresent(filters -> {
            Map<String, AttributeValue> expressionValues = new HashMap<>();
            String filterExpression = filters.entrySet().stream()
                    .map(entry -> {
                        String placeholder= ":"+ entry.getKey();
                        expressionValues.computeIfAbsent(
                                placeholder, k-> AttributeValue.builder().s(entry.getValue()).build());

                        return entry.getKey() + " = " + placeholder;
                    })
                    .collect(Collectors.joining(" AND "));

            scanRequestBuilder.filterExpression(Expression.builder()
                    .expression(filterExpression)
                    .expressionValues(expressionValues)
                    .build());
        });

        projectionAttribute.ifPresent(scanRequestBuilder :: attributesToProject);
        return table.scan(scanRequestBuilder.build()).items().stream().toList();
    }

    private Key getKey(String partitionKey, Optional<String> sortKey){
        Key.Builder keyBuilder = Key.builder().partitionValue(partitionKey);
        sortKey.ifPresent(keyBuilder :: sortValue);
        return keyBuilder.build();
    }

    private QueryConditional buildQueryConditional(String partitionKey, Optional<String> sortKey,
                                                   QueryOperator operator,
                                                   Optional<String> sortKeyEnd) {
        Key.Builder keyBuilder = Key.builder().partitionValue(partitionKey);
        return switch (operator) {
            case EQUALS -> {
                sortKey.ifPresent(keyBuilder :: sortValue);
                yield QueryConditional.keyEqualTo(keyBuilder.build());
            }
            case BEGINS_WITH -> {
                sortKey.ifPresent(keyBuilder :: sortValue);
                yield QueryConditional.sortBeginsWith(keyBuilder.build());
            }
            case GREATER_THAN -> {
                sortKey.ifPresent(keyBuilder :: sortValue);
                yield QueryConditional.sortGreaterThan(keyBuilder.build());
            }
            case GREATER_THAN_OR_EQUALS -> {
                sortKey.ifPresent(keyBuilder :: sortValue);
                yield QueryConditional.sortGreaterThanOrEqualTo(keyBuilder.build());
            }
            case LESS_THAN -> {
                sortKey.ifPresent(keyBuilder :: sortValue);
                yield QueryConditional.sortLessThan(keyBuilder.build());
            }
            case LESS_THAN_OR_EQUALS ->  {
                sortKey.ifPresent(keyBuilder :: sortValue);
                yield QueryConditional.sortLessThanOrEqualTo(keyBuilder.build());
            }
            case BETWEEN -> {
                if(sortKey.isEmpty() ||  sortKeyEnd.isEmpty()){
                    throw new IllegalArgumentException("Both sortKey and sortKeyEnd must be provided for BETWEEN queries.");
                }
                yield QueryConditional.sortBetween(
                        Key.builder().partitionValue(partitionKey).sortValue(sortKey.get()).build(),
                        Key.builder().partitionValue(partitionKey).sortValue(sortKeyEnd.get()).build()
                );
            }
        };
    }
}
