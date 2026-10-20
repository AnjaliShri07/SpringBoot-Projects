package com.dynamodb_springboot.dynamodb.controller;

import com.dynamodb_springboot.dynamodb.commons.Constant.QueryOperator;
import com.dynamodb_springboot.dynamodb.model.BaseModel;
import com.dynamodb_springboot.dynamodb.service.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Validated
@RequestMapping("/dynamoDb")
public abstract class BaseController<T extends BaseModel> {

    protected final BaseService<T> service;


    public BaseController(BaseService<T> service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create new item")
    public ResponseEntity<T> createItem(@RequestBody T entity) {
        service.createItem(entity);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    @Operation(summary = "Get Item")
    public ResponseEntity<T> getItem(@RequestParam String partitionKey,
                                     @RequestParam(required = false) Optional<String> sortKey) {
        T item = service.getItem(partitionKey, sortKey);
        return ObjectUtils.isEmpty(item) ? ResponseEntity.status(HttpStatus.NOT_FOUND).build():
                ResponseEntity.ok(item);
    }

    @PutMapping
    @Operation(summary = "update an item")
    public ResponseEntity<Void> updateItem (@RequestBody T entity){
        service.updateItem(entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete an Item")
    public ResponseEntity<Void> deleteItem(@RequestParam String partitionKey,
                                           @RequestParam(required = false) Optional<String> sortKey) {
        service.deleteItem(partitionKey, sortKey);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/query")
    @Operation(summary = "query items")
    public ResponseEntity<List<T>> query(@RequestParam String partitionKey,
                                   @RequestParam(required = false) Optional<String> sortKey,
                                   @RequestParam(required = false) Optional<String> indexName,
                                   @RequestParam(required = false) List<String> projectionAttributes,
                                   @RequestParam(required = false) QueryOperator queryOperator,
                                   @RequestParam(required = false) Optional<String> sortKeyEnd) {

        List<T> items = service.query(partitionKey, sortKey, indexName, Optional.ofNullable(projectionAttributes),
                queryOperator, sortKeyEnd);
        return items.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(items);
    }

    @GetMapping("/scan")
    @Operation(summary = "scan items")
    public ResponseEntity<List<T>> scan(@RequestParam(required = false) Map<String, String> filterAttributes,
                                     @RequestParam(required = false) List<String> projectionAttributes) {

        List<T> items = service.scan(Optional.ofNullable(filterAttributes),
                Optional.ofNullable(projectionAttributes));
        return items.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(items);
    }

}
