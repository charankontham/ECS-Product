package com.ecs.ecs_product.repository;

import com.ecs.ecs_product.entity.Product;
import com.ecs.ecs_product.entity.ProductBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductBrandRepository extends JpaRepository<ProductBrand, Integer> {

    @Query("SELECT pb FROM ProductBrand pb WHERE "
            + "pb.brandId <> 6 AND "
            + "(:searchValue IS NULL OR "
            + "LOWER(pb.brandName) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR "
            + "LOWER(pb.brandDescription) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR "
            + "STR(pb.brandId) = :searchValue )"
            + "ORDER BY pb.brandId DESC")
    Page<ProductBrand> findFilteredProductBrands(Pageable pageable, @Param("searchValue") String searchValue);
}
