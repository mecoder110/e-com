package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    ModelMapper mapper;
    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    CategoryServiceImpl categoryService;

    CategoryDTO categoryDTO = null;
    Category category = null;

    @BeforeEach
    public void beforeAll() {
        System.out.println("=== Before All ===");
        categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("Mobile");

        category = new Category();
        category.setCategoryName("Mobile");

    }

    @Test
    void createCategory() {
        Mockito.when(mapper.map(categoryDTO, Category.class))
                .thenReturn(category);

        Mockito.when(categoryRepository.save(category))
                .thenReturn(category);
        String expected = "Category created successfully";
        String save = categoryService.createCategory(categoryDTO);
        Assertions.assertEquals(expected, save);
    }

}