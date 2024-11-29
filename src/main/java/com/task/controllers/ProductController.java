package com.task.controllers;

import com.task.entities.Product;
import com.task.services.ProductService;
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
public class ProductController {

    @Autowired
    ProductService productService;


    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        System.out.println("ProductController >> getAllProducts..");
        return productService.getAllProducts(page, size);
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Long id) {
        return productService.getProduct(id);
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        return productService.deleteProduct(id);
    }


    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }
}
