package com.dynamodbcrud.cruddynamodb.service;

import com.dynamodbcrud.cruddynamodb.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ItemService {

    @Autowired
    private DynamoDbClient dynamoDbClient;
    private static final String TABLE_NAME = "item";



    // Create Item
    public boolean createItem(Item item) {
        try {
            // Prepare the item attributes to store in DynamoDB
            Map<String, AttributeValue> itemValues = Map.of(
                    "id", AttributeValue.builder().s(item.getId()).build(),
                    "name", AttributeValue.builder().s(item.getName()).build(),
                    "description", AttributeValue.builder().s(item.getDescription()).build()
            );
// Create the put item request
            PutItemRequest putItemRequest = PutItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .item(itemValues)
                    .build();
// Send the put request to DynamoDB
            dynamoDbClient.putItem(putItemRequest);
            return true;
        } catch (Exception e) {
            // Handle any exceptions (e.g., item already exists)
            System.out.println("Error creating item: " + e.getMessage());
            return false;
        }
    }

    /*public List<Map<String, String>> getAllItems() {
        List<Map<String, String>> allItems = new ArrayList<>();
        // Create a ScanRequest
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .build();

        // Scan the table and get the response
        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);

        // Iterate over the items and print them
        for (Map<String, AttributeValue> item : scanResponse.items()) {
            System.out.println(item);
            Map<String, String> itemAsString = convertToStringMap(item);
            allItems.add(itemAsString);
        }
        return allItems;
    }*/

    // Read Item by ID
    public Item findById(String id) {
        try {
            // Prepare the key to query
            Map<String, AttributeValue> key = Map.of(
                    "id", AttributeValue.builder().s(id).build()
            );
// Create get item request
            GetItemRequest getItemRequest = GetItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .key(key)
                    .build();
// Get item response
            GetItemResponse response = dynamoDbClient.getItem(getItemRequest);
            if (response.hasItem()) {
                // Convert the response to an Item object
                return new Item(
                        response.item().get("id").s(),
                        response.item().get("name").s(),
                        response.item().get("description").s()
                );
            } else {
                return null; // Return null if not found
            }
        } catch (Exception e) {
            // Handle any exceptions
            System.out.println("Error reading item: " + e.getMessage());
            return null;
        }
    }
    // Update Item
    public boolean updateItem(String id, Item item) {
        try {
            // Prepare the key and update expressions
            Map<String, AttributeValue> key = Map.of(
                    "id", AttributeValue.builder().s(id).build()
            );
            Map<String, AttributeValueUpdate> updatedValues = Map.of(
                    "name", AttributeValueUpdate.builder()
                            .value(AttributeValue.builder().s(item.getName()).build())
                            .action(AttributeAction.PUT)
                            .build(),
                    "description", AttributeValueUpdate.builder()
                            .value(AttributeValue.builder().s(item.getDescription()).build())
                            .action(AttributeAction.PUT)
                            .build()
            );
// Create update item request
            UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .key(key)
                    .attributeUpdates(updatedValues)
                    .build();
// Send the update request
            dynamoDbClient.updateItem(updateItemRequest);
            return true;
        } catch (Exception e) {
            // Handle any exceptions
            System.out.println("Error updating item: " + e.getMessage());
            return false;
        }
    }
    // Delete Item by ID
    public boolean deleteById(String id) {
        try {
            // Prepare the key for delete
            Map<String, AttributeValue> key = Map.of(
                    "id", AttributeValue.builder().s(id).build()
            );
// Create delete item request
            DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                    .tableName(TABLE_NAME)
                    .key(key)
                    .build();
// Send the delete request
            DeleteItemResponse deleteItemResponse = dynamoDbClient.deleteItem(deleteItemRequest);
            return deleteItemResponse.sdkHttpResponse().isSuccessful();
        } catch (Exception e) {
            // Handle any exceptions
            System.out.println("Error deleting item: " + e.getMessage());
            return false;
        }
    }

    private Map<String, String> convertToStringMap(Map<String, AttributeValue> item) {
        Map<String, String> itemAsString = new java.util.HashMap<>();
        for (Map.Entry<String, AttributeValue> entry : item.entrySet()) {
            itemAsString.put(entry.getKey(), entry.getValue().s());  // Converting AttributeValue to String
        }
        return itemAsString;
    }

}
