package com.ecs.ecs_product.repository;

import com.ecs.ecs_product.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer> {
    List<SubCategory> findAllByCategoryId(Integer categoryId);
    List<SubCategory> findAllBySubCategoryName(String subCategoryName);
}

