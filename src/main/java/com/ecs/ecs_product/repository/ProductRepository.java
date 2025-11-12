package com.ecs.ecs_product.repository;

import com.ecs.ecs_product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> getProductsByProductCategoryId(Integer categoryId);
    List<Product> getProductsByProductBrandId(Integer brandId);
    void deleteByProductCategoryId(Integer productCategoryId);
    List<Product> findByProductCategoryIdAndProductIdNot(Integer productCategoryId, Integer productId);

    @Query(value = """
    SELECT p.*
    FROM product p
    WHERE
        (:categoryId IS NULL OR p.product_category_id = :categoryId)
        AND (:subCategoryId IS NULL OR p.sub_category_id = :subCategoryId)
        AND (:brandId IS NULL OR p.product_brand_id = :brandId)
        AND (
            :searchValue IS NULL
            OR MATCH(p.product_name) AGAINST(:searchValue IN NATURAL LANGUAGE MODE)
        )
    ORDER BY
        CASE
            WHEN :searchValue IS NOT NULL THEN MATCH(p.product_name) AGAINST(:searchValue IN NATURAL LANGUAGE MODE)
            ELSE 0
        END DESC,
        p.date_added DESC
    """,
            countQuery = """
    SELECT COUNT(*)
    FROM product p
    WHERE
        (:categoryId IS NULL OR p.product_category_id = :categoryId)
        AND (:subCategoryId IS NULL OR p.sub_category_id = :subCategoryId)
        AND (:brandId IS NULL OR p.product_brand_id = :brandId)
        AND (
            :searchValue IS NULL
            OR MATCH(p.product_name) AGAINST(:searchValue IN NATURAL LANGUAGE MODE)
        )
    """,
            nativeQuery = true)
    Page<Product> findFilteredProducts(Pageable pageable,
                                       @Param("categoryId") Integer categoryId,
                                       @Param("subCategoryId") Integer subCategoryId,
                                       @Param("brandId") Integer brandId,
                                       @Param("searchValue") String searchValue);

//    @Query(value = """
//        SELECT p.*,
//        (
//            (MATCH(p.product_name) AGAINST (:keyword IN BOOLEAN MODE)) * 4 +
//            (CASE
//                WHEN LOWER(p.product_name) = LOWER(:keyword) THEN 3
//                WHEN LOWER(p.product_name) LIKE LOWER(CONCAT(:keyword, '%')) THEN 2
//                WHEN LOWER(p.product_name) LIKE LOWER(CONCAT('%', :keyword, '%')) THEN 1
//                ELSE 0
//             END)
//        ) AS relevance
//        FROM product p
//        WHERE MATCH(p.product_name) AGAINST (:keyword IN BOOLEAN MODE)
//           OR LOWER(p.product_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
//        ORDER BY relevance DESC
//        LIMIT 5
//        """, nativeQuery = true)
//    List<Product> searchTop5Products(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE p.productQuantity = 0 ORDER BY p.productName" )
    Page<Product> findAllOutOfStockProducts(Pageable pageable);
}
