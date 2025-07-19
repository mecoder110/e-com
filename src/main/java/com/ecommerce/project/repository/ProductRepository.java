package com.ecommerce.project.repository;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    Product findByCategory(Category category);

    List<Product> findByProductNameContainingIgnoreCase(String keyword);

   // Boolean findByProductName(String productName);

    Boolean existsByProductName(String productName);
}
