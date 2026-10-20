package com.dynamodb_springboot.dynamodb.service;

import com.dynamodb_springboot.dynamodb.base.ReadWriteInterface;
import com.dynamodb_springboot.dynamodb.commons.Constant;
import com.dynamodb_springboot.dynamodb.commons.DateTimeUtil;
import com.dynamodb_springboot.dynamodb.model.BaseModel;
import com.dynamodb_springboot.dynamodb.repository.BaseRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.dynamodb_springboot.dynamodb.commons.Constant.*;

@Slf4j
public abstract class BaseService<T extends BaseModel> implements ReadWriteInterface<T> {

    // add exception and validator also in all methods

    protected final BaseRepository<T> repository;
    /*protected final ReadWriteAbstractValidator<T> validator;*/

    public BaseService(BaseRepository<T> repository) {
        this.repository = repository;
    }


    @Override
    public void createItem(T entity) {
        try {
            //validator
            setCommonAttributes(entity);
            setKeyAttribute(entity, "partition_key_1", UUID.randomUUID() + HASH + entity.getVersion());
            repository.createItem(entity);
            createLatestItemCopy(entity);
            log.info("Entity created Successfully: {}", entity);
        } catch (Exception e) {
            throw new RuntimeException("Error");
        }
    }

    @Override
    public T getItem(String partitionKey, Optional<String> sortKey) {
        try {
            return repository.getItem(partitionKey, sortKey);
        } catch (Exception e) {
            throw new RuntimeException("Error");
        }
    }

    @Override
    public void updateItem(T entity) {
        try {
            updatedItemUtil(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error");
        }
    }

    @Override
    public void deleteItem(String partitionKey, Optional<String> sortKey) {
        try {
            repository.deleteItem(partitionKey, sortKey);
        } catch (Exception e) {
            throw new RuntimeException("Error");
        }
    }

    @Override
    public List<T> query(String partitionKey, Optional<String> sortKey, Optional<String> indexName,
                         Optional<List<String>> projectionAttribute, Constant.QueryOperator queryOperator,
                         Optional<String> sortKeyEnd) {
        try {
            List<T> result = repository.query(partitionKey, sortKey, indexName, projectionAttribute,
                    queryOperator, sortKeyEnd);


            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error");
        }
    }

    @Override
    public List<T> scan(Optional<Map<String, String>> filterAttributes
            , Optional<List<String>> projectionAttribute) {
        try {
            List<T> result = repository.scan(filterAttributes, projectionAttribute);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error");
        }
    }

    protected void setCommonAttributes(T entity) {
        entity.setVersion(VERSION_V1);
        entity.setCreatedBy(SYSTEM);
        entity.setUpdatedBy(SYSTEM);
        entity.setCreatedAt(DateTimeUtil.getCurrentTimestamp());
        entity.setUpdatedBy(DateTimeUtil.getCurrentTimestamp());
    }

    protected void setKeyAttribute(T entity, String partitionKey, String sortKey) {
        entity.setPartitionKey(partitionKey);
        entity.setSortKey(sortKey);
    }

    protected void updatedItemUtil(T entity) {
        updatePrevItemVersionStatus(entity);
        updateCurrentItemSortKeyAndVersion(entity);
        updateLatestRecord(entity);
    }

    private void updatePrevItemVersionStatus(T entity) {
        String updatedSortKey = getUpdatedSortKey(entity.getSortKey(), entity.getVersion());
        T item = repository.getItem(entity.getPartitionKey(), Optional.of(updatedSortKey));
        item.setIsLatestVersion(Boolean.FALSE.toString());
        repository.updateItem(item);
    }

    private void updateCurrentItemSortKeyAndVersion(T entity) {
        String newVersion = getUpdatedVersion(entity.getVersion());
        String newSortKey = getUpdatedSortKey(entity.getSortKey(), newVersion);
        entity.setSortKey(newSortKey);
        entity.setVersion(newVersion);
        entity.setIsLatestVersion(Boolean.TRUE.toString());
        repository.updateItem(entity);
    }

    private void updateLatestRecord(T entity) {
        String sortKey = getUpdatedSortKey(entity.getSortKey(), LATEST);
        T latestItem = repository.getItem(entity.getPartitionKey(), Optional.of(entity.getSortKey()));
        String newVersion = entity.getVersion();
        latestItem.setSortKey(sortKey);
        latestItem.setVersion(newVersion);
        repository.updateItem(latestItem);
    }

    private void createLatestItemCopy(T entity) {
        T latestVersionItem = (T) entity.deepCopy();
        String sortKey = getUpdatedSortKey(latestVersionItem.getSortKey(), LATEST);
        latestVersionItem.setSortKey(sortKey);
        repository.createItem(latestVersionItem);
    }

    private String getUpdatedSortKey(String prevSortKey, String updatedVersion) {

        String[] parts;
        if (prevSortKey.contains(HASH + VERSION))
            parts = prevSortKey.split(HASH + VERSION);
        else
            parts = prevSortKey.split(HASH + LATEST);

        String uuid = parts[0];
        return uuid + HASH + updatedVersion;
    }

    private String getUpdatedVersion(String version) {
        int versionNumber = Integer.parseInt(version.substring(1));
        int newVersionNumber = versionNumber + 1;
        return VERSION + newVersionNumber;
    }
}
