package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;

public interface CategoryService {


    CategoryResponse getCategoryList(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy);

    void createCategory(CategoryDTO categoryDTO);

    String deleteCategory(Long id);

    String updateCategory(Category category, Long id);
}
