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

    @Query("SELECT p FROM Product p WHERE "
            + "(:categoryId IS NULL OR p.productCategoryId = :categoryId) AND "
            + "(:subCategoryId IS NULL OR p.subCategoryId = :subCategoryId) AND "
            + "(:brandId IS NULL OR p.productBrandId = :brandId) AND "
            + "(:searchValue IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :searchValue, '%')))")
    Page<Product> findFilteredProducts(Pageable pageable,
                                       @Param("categoryId") Integer categoryId,
                                       @Param("subCategoryId") Integer subCategoryId,
                                       @Param("brandId") Integer brandId,
                                       @Param("searchValue") String searchValue);
}
