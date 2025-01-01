package com.ecs.ecs_product.repository;

import com.ecs.ecs_product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> getProductsByProductCategoryId(Integer categoryId);
    void deleteByProductCategoryId(Integer productCategoryId);
}
