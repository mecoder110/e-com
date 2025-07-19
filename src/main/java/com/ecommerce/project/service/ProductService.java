package com.ecommerce.project.service;

import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    ProductResponse getAllProduct();

    ProductDTO saveProduct(ProductDTO productDTO, Long categoryId);

    ProductDTO getProductByCategory(Long categoryId);

    List<ProductDTO> getProductByKeyword(String keyword);

    ProductDTO deleteProductById(Long productId);

    ProductDTO updateProductById(Long productId, ProductDTO productDTO);

    ProductDTO updateProductImage(Long productId, MultipartFile multipartFile) throws IOException;
}
