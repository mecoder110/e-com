package com.ecommerce.project.controller;

import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    // List of Product
    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProduct() {
        ProductResponse productResponse = productService.getAllProduct();
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    // Create Product with associate Category-Id
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> getAllProduct(@RequestBody ProductDTO productDTO,
                                                    @PathVariable Long categoryId) {
        ProductDTO saveProductDTO = productService.saveProduct(productDTO, categoryId);
        return new ResponseEntity<>(saveProductDTO, HttpStatus.CREATED);
    }

    // Get Product by Associated Category-Id
    @GetMapping("/public/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long categoryId) {
        ProductDTO getProductDTO = productService.getProductByCategory(categoryId);
        return new ResponseEntity<>(getProductDTO, HttpStatus.OK);
    }

    // Get Product by Keywords
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<List<ProductDTO>> getProductByKeywords(@PathVariable String keyword) {
        List<ProductDTO> productDTO = productService.getProductByKeyword(keyword);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProductDTO = productService.updateProductById(productId, productDTO);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<ProductDTO> deleteProductById(@PathVariable(name = "id") Long productId) {
        ProductDTO productDTO = productService.deleteProductById(productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam MultipartFile image) throws IOException {
        ProductDTO updatedProductDTO = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }
}
