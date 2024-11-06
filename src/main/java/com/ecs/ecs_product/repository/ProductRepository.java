package com.ecs.ecs_product.repository;

import com.ecs.ecs_product.dto.ProductFinalDto;
import com.ecs.ecs_product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    void deleteByProductCategoryId(Integer productCategoryId);
    void deleteByProductBrandId(Integer productBrandId);
}
