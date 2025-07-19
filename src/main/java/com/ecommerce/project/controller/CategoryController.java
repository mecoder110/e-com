package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    // List of Category
    @GetMapping("/api/public/categories")
    public ResponseEntity<CategoryResponse> getCategoryList(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER) String sortOrder,
                                                            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY) String sortBy) {
        CategoryResponse categoryList = categoryService.getCategoryList(pageNumber, pageSize, sortOrder, sortBy);
        return ResponseEntity.ok().body(categoryList);
    }

    // Create Category
    @PostMapping("/api/public/categories")
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDTO category) {
        categoryService.createCategory(category);
        return ResponseEntity.ok().body("Category created successfully!!!");
    }

    // Delete Category By ID
    @DeleteMapping("/api/admin/categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        try {
            String status = categoryService.deleteCategory(id);
            return new ResponseEntity<>(status, HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
        }
    }

    // Update Category by Id
    @PutMapping("/api/admin/categories/{id}")
    public ResponseEntity<String> updateCategory(@RequestBody Category category, @PathVariable Long id) {
        try {
            String status = categoryService.updateCategory(category, id);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
        }

    }


}

