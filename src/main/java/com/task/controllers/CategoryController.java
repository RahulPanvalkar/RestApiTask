package com.task.controllers;

import com.task.entities.Category;
import com.task.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    CategoryService categoryService;


    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        System.out.println("CategoryController >> getAllCategories..");
        return categoryService.getAllCategories(page, size);
    }

    @PostMapping("/categories")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }


    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getCategory(@PathVariable("id") Long id) {
        return categoryService.getCategory(id);
    }


    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
        return categoryService.deleteCategory(id);
    }


    @PutMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }
}
