package com.task.services;

import com.task.entities.Category;
import com.task.entities.Product;
import com.task.repositories.CategoryRepo;
import com.task.repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepo productRepo;
    @Autowired
    CategoryRepo categoryRepo;

    public ResponseEntity<?> getAllProducts(int page, int size) {
        System.out.println("getAllProducts >> page : " + page + " & size : " + size);
        Map<String, Object> response = new HashMap<>();

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> pageProducts = productRepo.findAll(pageable);

        if (pageProducts.isEmpty()) {
            response.put("success", false);
            response.put("error", createErrorMap(400, "No products found in the system."));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("success", true);
        response.put("message", "Request successful");
        response.put("data", pageProducts.get());   // to get categories only and not include pagination metadata

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<?> addProduct(Product product) {

        System.out.println("addProduct >> product : \n" + product);

        Map<String, Object> response = new HashMap<>();

        try {
            // Input validation
            if (product.getProductName() == null || product.getProductName().isEmpty()) {
                response.put("success", false);
                response.put("error", createErrorMap(400, "Product name is required."));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (product.getDescription() == null || product.getDescription().isEmpty()) {
                response.put("success", false);
                response.put("error", createErrorMap(400, "Product description is required."));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (product.getPrice() <= 0) {
                response.put("success", false);
                response.put("error", createErrorMap(400, "Product price must be greater than 0."));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (product.getQuantity() <= 0) {
                response.put("success", false);
                response.put("error", createErrorMap(400, "Product quantity must be greater than 0."));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Optional<Product> existingProductOptional = productRepo.findByProductName(product.getProductName());
            if (existingProductOptional.isPresent()) {
                response.put("success", false);
                response.put("error", createErrorMap(400, "Product name already exists."));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Fetch the category by ID
            Optional<Category> categoryOptional = categoryRepo.findById(product.getCategory().getCategoryId());
            if (categoryOptional.isEmpty()) {
                response.put("success", false);
                response.put("error", createErrorMap(400, "Product category not found in the system."));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            product.setCategory(categoryOptional.get());
            Product savedProduct = productRepo.save(product);

            response.put("success", true);
            response.put("message", "Product added successfully");
            response.put("data", savedProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", createErrorMap(500, "Something went wrong."));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }


    public ResponseEntity<?> getProduct(Long id) {
        System.out.println("getProduct >> id :: " + id);
        Map<String, Object> response = new HashMap<>();

        // Fetch the category by ID
        Optional<Product> productOptional = productRepo.findById(id);

        if (productOptional.isEmpty()) {
            response.put("success", false);
            response.put("error", createErrorMap(404, "Product not found in the system."));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("success", true);
        response.put("message", "Request successful");
        response.put("data", productOptional.get());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<?> deleteProduct(Long id) {
        System.out.println("deleteProduct >> product Id : " + id);

        Map<String, Object> response = new HashMap<>();

        // Fetch the product by ID
        Optional<Product> productOptional = productRepo.findById(id);
        System.out.println(productOptional);
        if (productOptional.isEmpty()) {
            response.put("success", false);
            response.put("error", createErrorMap(404, "Product not found in the system."));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Delete the category
        productRepo.deleteById(id);

        response.put("success", true);
        response.put("message", "Product deleted successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    public ResponseEntity<?> updateProduct(Long id, Product product) {

        System.out.println("updateProduct >> id : [" + id + "]\n" + product);

        Map<String, Object> response = new HashMap<>();

        try {
            // Input validation
            if (product.getProductName() == null || product.getProductName().isEmpty()) {
                response.put("success", false);
                response.put("error", createErrorMap(400, "Product name is required."));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (product.getDescription() == null || product.getDescription().isEmpty()) {
                response.put("success", false);
                response.put("error", createErrorMap(400, "Product description is required."));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (product.getPrice() <= 0) {
                response.put("success", false);
                response.put("error", createErrorMap(400, "Product price must be greater than 0."));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (product.getQuantity() <= 0) {
                response.put("success", false);
                response.put("error", createErrorMap(400, "Product quantity must be greater than 0."));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (product.getCategory() == null) {
                response.put("success", false);
                response.put("error", createErrorMap(400, "Product category is required."));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Check if the product with same name exists
            Optional<Product> existingProductOptional = productRepo.findByProductName(product.getProductName());
            if (existingProductOptional.isPresent() && existingProductOptional.get().getProductId() != id) {
                response.put("success", false);
                response.put("error", createErrorMap(400, "Product name already exists."));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Check if the Category exists
            Optional<Category> categoryOptional = categoryRepo.findById(product.getCategory().getCategoryId());
            System.out.println(categoryOptional);
            if (categoryOptional.isEmpty()) {
                response.put("success", false);
                response.put("error", createErrorMap(400, "Product category not found in the system."));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Check if the product exists
            existingProductOptional = productRepo.findById(id);
            System.out.println(existingProductOptional);
            if (existingProductOptional.isEmpty()) {
                response.put("success", false);
                response.put("error", createErrorMap(404, "Product not found in the system."));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }


            // Update the product
            Product updatedProduct = existingProductOptional.get();
            updatedProduct.setProductName(product.getProductName());
            updatedProduct.setQuantity(product.getQuantity());
            updatedProduct.setPrice(product.getPrice());
            updatedProduct.setCategory(categoryOptional.get());

            // save updated product
            Product savedProduct = productRepo.save(updatedProduct);

            response.put("success", true);
            response.put("message", "Product updated successfully");
            response.put("data", savedProduct);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", createErrorMap(500, "Something went wrong."));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }


    private Map<String, Object> createErrorMap(int code, String details) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("code", code);
        errorMap.put("details", details);
        return errorMap;
    }
}
