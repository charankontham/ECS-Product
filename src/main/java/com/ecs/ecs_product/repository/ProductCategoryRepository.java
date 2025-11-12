package com.ecs.ecs_product.repository;

import com.ecs.ecs_product.entity.ProductBrand;
import com.ecs.ecs_product.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    @Query("SELECT pc FROM ProductCategory pc WHERE "
            + "pc.categoryId <> 24 AND "
            + "(:searchValue IS NULL OR "
            + "LOWER(pc.productCategoryName) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR "
            + "STR(pc.categoryId) = :searchValue )"
            + "ORDER BY pc.categoryId DESC")
    Page<ProductCategory> findFilteredProductCategories(Pageable pageable, @Param("searchValue") String searchValue);

    @Query(value = """
        SELECT pc.*,
        (
            (MATCH(pc.category_name) AGAINST (:keyword IN BOOLEAN MODE)) * 4 +
            (CASE
                WHEN LOWER(pc.category_name) = LOWER(:keyword) THEN 3
                WHEN LOWER(pc.category_name) LIKE LOWER(CONCAT(:keyword, '%')) THEN 2
                WHEN LOWER(pc.category_name) LIKE LOWER(CONCAT('%', :keyword, '%')) THEN 1
                ELSE 0
             END)
        ) AS relevance
        FROM product_category pc
        WHERE MATCH(pc.category_name) AGAINST (:keyword IN BOOLEAN MODE)
           OR LOWER(pc.category_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ORDER BY relevance DESC
        LIMIT 5
        """, nativeQuery = true)
    List<ProductCategory> searchTop2Categories(@Param("keyword") String keyword);
}
