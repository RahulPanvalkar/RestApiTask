package com.task.services;

import com.task.entities.Category;
import com.task.repositories.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepo categoryRepo;

    public ResponseEntity<?> getAllCategories(int page, int size) {
        System.out.println("getAllCategories >> page : " + page + " & size : " + size);
        Map<String, Object> response = new HashMap<>();

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Category> pageCategories = categoryRepo.findAll(pageable);

            if (pageCategories.isEmpty()) {
                response.put("success", false);
                response.put("error", createErrorMap(404, "No categories found in the system."));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put("success", true);
            response.put("message", "Request successful");
            response.put("data", pageCategories.get());   // to get categories only and not include pagination metadata

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", createErrorMap(500, "An error occurred while fetching categories."));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    public ResponseEntity<?> addCategory(Category category) {
        System.out.println("addCategory >> category : \n" + category);

        Map<String, Object> response = new HashMap<>();

        try {
            // Input validation
            if (category.getCategoryName() == null || category.getCategoryName().isEmpty()) {
                response.put("success", false);
                response.put("error", createErrorMap(400, "Category name is required."));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (category.getDescription() == null || category.getDescription().isEmpty()) {
                response.put("success", false);
                response.put("error", createErrorMap(400, "Category description is required."));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Fetch the category by ID
            Optional<Category> categoryOptional = categoryRepo.findByCategoryName(category.getCategoryName());
            if (categoryOptional.isPresent()) {
                response.put("success", false);
                response.put("error", createErrorMap(400, "This Category already exist."));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Save the Category
            Category savedCategory = categoryRepo.save(category);

            response.put("success", true);
            response.put("message", "Product added successfully");
            response.put("data", savedCategory);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", createErrorMap(500, "An error occurred while adding the category."));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<?> getCategory(Long id) {
        System.out.println("getCategory >> id :: " + id);
        Map<String, Object> response = new HashMap<>();

        try {
            // Fetch the category by ID
            Optional<Category> categoryOptional = categoryRepo.findById(id);

            if (categoryOptional.isEmpty()) {
                response.put("success", false);
                response.put("error", createErrorMap(404, "Category not found in the system."));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put("success", true);
            response.put("message", "Request successful");
            response.put("data", categoryOptional.get());

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", createErrorMap(500, "An error occurred while fetching a category."));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Deleting a category will automatically remove all associated products due to the configured cascading relationship.
    @Transactional
    public ResponseEntity<?> deleteCategory(Long id) {
        System.out.println("deleteCategory >> id :: " + id);
        Map<String, Object> response = new HashMap<>();

        try {
            // Fetch the category by ID
            Optional<Category> categoryOptional = categoryRepo.findById(id);

            if (categoryOptional.isEmpty()) {
                response.put("success", false);
                response.put("error", createErrorMap(404, "Category not found in the system."));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Delete the category
            categoryRepo.deleteById(id);

            response.put("success", true);
            response.put("message", "Category and its products deleted successfully");

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", createErrorMap(500, "An error occurred while deleting the category."));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    public ResponseEntity<?> updateCategory(Long id, Category category) {
        System.out.println("updateCategory >> id : [" + id + "]\n" + category);

        Map<String, Object> response = new HashMap<>();

        try {
            // Input validation
            if (category.getCategoryName() == null || category.getCategoryName().isEmpty()) {
                response.put("success", false);
                response.put("error", createErrorMap(400, "Category name is required."));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (category.getDescription() == null || category.getDescription().isEmpty()) {
                response.put("success", false);
                response.put("error", createErrorMap(400, "Category description is required."));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Fetch the category by ID
            Optional<Category> existingCategoryOpt = categoryRepo.findById(id);
            if (existingCategoryOpt.isEmpty()) {
                response.put("success", false);
                response.put("error", createErrorMap(404, "Category not found."));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Check if the new category name already exists for another category
            Optional<Category> categoryWithSameName = categoryRepo.findByCategoryName(category.getCategoryName());
            if (categoryWithSameName.isPresent() && (categoryWithSameName.get().getCategoryId() != id)) {
                response.put("success", false);
                response.put("error", createErrorMap(400, "Category name already exists."));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Update category details
            Category updatedCategory = existingCategoryOpt.get();
            updatedCategory.setCategoryName(category.getCategoryName());
            updatedCategory.setDescription(category.getDescription());

            // Save the updated category
            categoryRepo.save(updatedCategory);

            response.put("success", true);
            response.put("message", "Category updated successfully");
            response.put("data", updatedCategory);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", createErrorMap(500, "An error occurred while updating the category."));
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
