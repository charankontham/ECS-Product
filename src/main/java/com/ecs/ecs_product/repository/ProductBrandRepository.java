package com.ecs.ecs_product.repository;

import com.ecs.ecs_product.entity.Product;
import com.ecs.ecs_product.entity.ProductBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductBrandRepository extends JpaRepository<ProductBrand, Integer> {

    @Query("SELECT pb FROM ProductBrand pb WHERE "
            + "pb.brandId <> 6 AND "
            + "(:searchValue IS NULL OR "
            + "LOWER(pb.brandName) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR "
            + "LOWER(pb.brandDescription) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR "
            + "STR(pb.brandId) = :searchValue )"
            + "ORDER BY pb.brandId DESC")
    Page<ProductBrand> findFilteredProductBrands(Pageable pageable, @Param("searchValue") String searchValue);

    @Query(value = """
        SELECT pb.*,
        (
            (MATCH(pb.brand_name) AGAINST (:keyword IN BOOLEAN MODE)) * 4 +
            (CASE
                WHEN LOWER(pb.brand_name) = LOWER(:keyword) THEN 3
                WHEN LOWER(pb.brand_name) LIKE LOWER(CONCAT(:keyword, '%')) THEN 2
                WHEN LOWER(pb.brand_name) LIKE LOWER(CONCAT('%', :keyword, '%')) THEN 1
                ELSE 0
             END)
        ) AS relevance
        FROM product_brand pb
        WHERE MATCH(pb.brand_name) AGAINST (:keyword IN BOOLEAN MODE)
           OR LOWER(pb.brand_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ORDER BY relevance DESC
        LIMIT 5
        """, nativeQuery = true)
    List<ProductBrand> searchTop2Brands(@Param("keyword") String keyword);
}
