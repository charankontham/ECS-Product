package com.ecs.ecs_product.service.interfaces;

import com.ecs.ecs_product.dto.SubCategoryDto;
import com.ecs.ecs_product.dto.SubCategoryEnriched;
import com.ecs.ecs_product.entity.SubCategory;

import java.util.List;

public interface ISubCategoryService {
    List<SubCategoryDto> getAllSubCategories();
    List<SubCategoryDto> getSubCategoriesByCategoryId(Integer categoryId);
    SubCategoryDto getSubCategoryById(Integer subCategoryId);
    SubCategoryEnriched getEnrichedSubCategoryById(Integer subCategoryId);
    List<SubCategoryDto> getAllSubCategoriesByName(String name);
    Object addSubCategory(SubCategoryDto subCategoryDto);
    Object updateSubCategory(SubCategoryDto subCategoryDto);
    void deleteSubCategory(Integer subCategoryId);
}
