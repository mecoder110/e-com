package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.NoResourceFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    String path;

    @Override
    public ProductResponse getAllProduct() {
        log.info("InSide Product-Service getAllProduct");
        List<Product> allProduct = productRepository.findAll();
        if (allProduct.isEmpty()) throw new NoResourceFoundException("Product", "No Product found", 00L);
        List<ProductDTO> productDtos = allProduct.stream().map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        log.info("Product translated to DTOs");
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDtos);
        log.info(productResponse.toString());
        return productResponse;
    }

    @Override
    public ProductDTO saveProduct(ProductDTO productDTO, Long categoryId) {
        Boolean isProductExist = productRepository.existsByProductName(productDTO.getProductName());

        if (isProductExist) throw new APIException("Inserted Product dublicate, Already Exist");

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NoResourceFoundException("Category", "Not found", categoryId));
        Product product = modelMapper.map(productDTO, Product.class);

        Double specialPrice = product.getPrice() * (1 - product.getDiscount() / 100.0);

        product.setSpecialPrice(specialPrice);
        product.setCategory(category);
        Product save = productRepository.save(product);
        ProductDTO saveDTO = modelMapper.map(save, ProductDTO.class);
        System.out.println("===> " + saveDTO.getSpecialPrice());
        return saveDTO;
    }

    @Override
    public ProductDTO getProductByCategory(Long categoryId) {
        log.info("Inside getProductByCategory");
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NoResourceFoundException("Category", "categoryId", categoryId));
        Product productFromDb = Optional.ofNullable(productRepository.findByCategory(category)).orElseThrow(() -> new NoResourceFoundException("Product", "productId", ""));

        log.info("{Product from DB ==> }  " + productFromDb.toString());
        ProductDTO productDto = modelMapper.map(productFromDb, ProductDTO.class);
        return productDto;
    }

    @Override
    public List<ProductDTO> getProductByKeyword(String keyword) {
        List<Product> matchProduct = productRepository.findByProductNameContainingIgnoreCase(keyword);

        List<ProductDTO> collect = matchProduct.stream().map(p -> modelMapper.map(p, ProductDTO.class)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public ProductDTO deleteProductById(Long productId) {
        log.info("Inside deleteProductById");
        Product product = productRepository.findById(productId).orElseThrow(() -> new NoResourceFoundException("Product", "Product", productId));

        productRepository.delete(product);
        log.info("Product Deleted Successfully : " + productId);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductById(Long productId, ProductDTO productDTO) {
        log.info("Inside updateProductById");
        Product product = productRepository.findById(productId).orElseThrow(() -> new NoResourceFoundException("Product", "Product", productId));

        product.setProductId(productId);
        product.setProductName(productDTO.getProductName());
        Double specialPrice = productDTO.getPrice() * (1 - productDTO.getDiscount() / 100.0);
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setSpecialPrice(specialPrice);
        product.setDiscount(productDTO.getDiscount());
        product.setQuantity(productDTO.getQuantity());

        Product updatedProduct = productRepository.save(product);
        log.info("Product saved successfully : " + updatedProduct.toString());
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile file) throws IOException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NoResourceFoundException("Product", "Product", productId));

        String fileName = fileService.uploadImageToServer(path, file);
        product.setImageUrl(fileName);
        Product save = productRepository.save(product);
        return modelMapper.map(save, ProductDTO.class);
    }


}
