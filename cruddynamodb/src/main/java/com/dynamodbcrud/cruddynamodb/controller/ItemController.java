package com.dynamodbcrud.cruddynamodb.controller;

import com.dynamodbcrud.cruddynamodb.model.Item;
import com.dynamodbcrud.cruddynamodb.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/create")
    public ResponseEntity<Boolean> createItem(@RequestBody Item item) {
        return ResponseEntity.ok(itemService.createItem(item));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItem(@PathVariable("id") String id) {
        Optional<Item> item = Optional.ofNullable(itemService.findById(id));
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

   /* @GetMapping("/get")
    public ResponseEntity<List<Map<String, String>>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }*/

    // Update Item
    @PutMapping("/update/{id}/{value}")
    public String updateItem(@PathVariable String id, @RequestBody Item items) {
        boolean isUpdated = itemService.updateItem(id, items);
        if (isUpdated) {
            return "Item with ID " + id + " updated successfully.";
        } else {
            return "Failed to update item with ID " + id;
        }
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable("id") String id) {
        itemService.deleteById(id);
        return ResponseEntity.ok("Item deleted successfully");
    }
}
