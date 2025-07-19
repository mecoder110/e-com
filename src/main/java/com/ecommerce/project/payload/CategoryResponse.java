package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private List<CategoryDTO> categoryDTOS;

    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private boolean isLastPage;
}
