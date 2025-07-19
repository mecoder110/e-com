package com.ecommerce.project.payload;

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
    private String productName;
    private String imageUrl;
    private String description;
    private Integer quantity;
    private Double price;
    private Double discount;
    private Double specialPrice;
}

