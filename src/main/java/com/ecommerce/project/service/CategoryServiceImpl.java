package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.NoResourceFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Page<Category> all = categoryRepository.findAll(pageDetails);
        //List<Category> all = categoryRepository.findAll();
        if (all == null || all.isEmpty()) {
            throw new APIException("Category not created yet to get");
        }
        List<CategoryDTO> listCategoryDto = all.stream().map(c ->
                mapper.map(c, CategoryDTO.class)
        ).toList();

        CategoryResponse responseCategory = new CategoryResponse();
        responseCategory.setCategoryDTOS(listCategoryDto);
        responseCategory.setPageNumber(pageDetails.getPageNumber());
        responseCategory.setPageSize(pageDetails.getPageSize());
        responseCategory.setTotalElements(all.getTotalElements());
        responseCategory.setLastPage(all.isLast());

        return responseCategory;
    }

    @Override
    public void createCategory(CategoryDTO categoryDTO) {
        Category category = mapper.map(categoryDTO, Category.class);

        Category getCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (getCategory != null) {
            throw new APIException("Category is already exist with category name " + category.getCategoryName());
        }
        Category save = categoryRepository.save(category);
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
