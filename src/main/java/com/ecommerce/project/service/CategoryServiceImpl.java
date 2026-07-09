package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.NoResourceFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ModelMapper mapper;

    @Override
    public CategoryResponse getCategoryList(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        //List<Category> categoryPage = categoryRepository.findAll();
        List<Category> categories = categoryPage.getContent();
        if (categories.isEmpty()) {
            throw new APIException("Category not created yet");
        }
        List<CategoryDTO> listCategoryDto = categories.stream().map(
                category -> {
                    return mapper.map(category, CategoryDTO.class);

                }
        ).toList();

        CategoryResponse responseCategory = new CategoryResponse();
        responseCategory.setCategoryDTOS(listCategoryDto);
        responseCategory.setPageNumber(pageDetails.getPageNumber());
        responseCategory.setPageSize(pageDetails.getPageSize());
        responseCategory.setTotalElements(categoryPage.getTotalElements());
        responseCategory.setLastPage(categoryPage.isLast());

        return responseCategory;
    }

    @Override
    public String createCategory(CategoryDTO categoryDTO) {
        Category category = mapper.map(categoryDTO, Category.class);
        log.info("=== Indisde Create Category ===");
        Category getCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (getCategory != null) {
            throw new APIException("Category is already exist with category name " + category.getCategoryName());
        }
        Category save = categoryRepository.save(category);
        if (save.getCategoryName() == null) {
            throw new APIException("Failed to create category with category name " + category.getCategoryName());
        }
        return "Category created successfully";
    }

    @Override
    public String deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoResourceFoundException("Category", " - ", id));
        categoryRepository.delete(category);

        return "category deleted successfully";
    }

    @Override
    public String updateCategory(Category category, Long id) {

        Category existCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NoResourceFoundException("Category", category.getCategoryName(), id));
        category.setCategoryId(id);
        categoryRepository.save(category);
        return "Category updated successfully";

    }
}
