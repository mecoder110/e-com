package com.ecommerce.project.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class ProductDTO {


    private Long productId;

    @NotBlank(message = "Product Name can not be empty")
    @Size(min = 2, max = 500, message = "Product Name should be 2 to 50 character")
    private String productName;
    private String imageUrl;

    @Size(max = 1000, message = "Description should not be beyond 1000 character")
    private String description;
    private Integer quantity;
    private Double price;
    private Double discount;
    private Double specialPrice;
}

