package com.ecs.ecs_product.repository;

import com.ecs.ecs_product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    void deleteByProductCategoryId(Integer productCategoryId);
}
