package com.dynamodb_springboot.dynamodb.base;

import com.dynamodb_springboot.dynamodb.commons.Constant;
import com.dynamodb_springboot.dynamodb.model.BaseModel;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReadWriteInterface<T extends BaseModel> {
    void createItem(T entity);

    T getItem(String partitionKey, Optional<String> sortKey);

    void updateItem(T entity);

    void deleteItem(String partitionKey, Optional<String> sortKey);

    List<T> query(String partitionKey, Optional<String> sortKey, Optional<String> indexName,
                  Optional<List<String>> projectionAttribute, Constant.QueryOperator queryOperator,
                  Optional<String> sortKeyEnd);

    List<T> scan(Optional<Map<String, String>> filterAttributes,
                 Optional<List<String>> projectionAttribute);
}
