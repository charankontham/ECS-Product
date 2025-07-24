package com.ecs.ecs_product.repository;

import com.ecs.ecs_product.entity.ProductBrand;
import com.ecs.ecs_product.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    @Query("SELECT pc FROM ProductCategory pc WHERE "
            + "pc.categoryId <> 24 AND "
            + "(:searchValue IS NULL OR "
            + "LOWER(pc.productCategoryName) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR "
            + "STR(pc.categoryId) = :searchValue )")
    Page<ProductCategory> findFilteredProductCategories(Pageable pageable, @Param("searchValue") String searchValue);
}
