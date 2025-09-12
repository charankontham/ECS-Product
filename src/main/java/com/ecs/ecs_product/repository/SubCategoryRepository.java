package com.ecs.ecs_product.repository;

import com.ecs.ecs_product.entity.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer> {
    List<SubCategory> findAllByCategoryId(Integer categoryId);
    List<SubCategory> findAllBySubCategoryName(String subCategoryName);

    @Query("""
            SELECT sc FROM SubCategory sc WHERE
            ( sc.subCategoryId <> 55 AND (:categoryId IS NULL OR sc.categoryId = :categoryId)) AND
            ( :searchValue IS NULL OR LOWER(sc.subCategoryName) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR
            LOWER(sc.subCategoryDescription) LIKE LOWER(CONCAT('%', :searchValue, '%')) OR
            STR(sc.subCategoryId) = :searchValue )
    """)
    Page<SubCategory> findFilteredSubCategories(Pageable pageable,
                                       @Param("categoryId") Integer categoryId,
                                       @Param("searchValue") String searchValue);
}

