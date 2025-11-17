package com.ecs.ecs_product.service.interfaces;

import com.ecs.ecs_product.dto.SubCategoryDto;
import com.ecs.ecs_product.dto.SubCategoryEnriched;
import com.ecs.ecs_product.entity.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISubCategoryService {
    List<SubCategoryDto> getAllSubCategories();
    List<SubCategoryEnriched> getAllSubCategoriesEnriched();
    List<SubCategoryDto> getSubCategoriesByCategoryId(Integer categoryId);
    Page<SubCategoryDto> getAllSubCategoriesWithPagination(Pageable pageable, Integer categoryId, String searchValue);
    SubCategoryDto getSubCategoryById(Integer subCategoryId);
    SubCategoryEnriched getEnrichedSubCategoryById(Integer subCategoryId);
    List<SubCategoryDto> getAllSubCategoriesByName(String name);
    Object addSubCategory(SubCategoryDto subCategoryDto);
    Object updateSubCategory(SubCategoryDto subCategoryDto);
    void deleteSubCategory(Integer subCategoryId);
}
