package com.ecs.ecs_product.mapper;

import com.ecs.ecs_product.dto.ProductCategoryDto;
import com.ecs.ecs_product.dto.SubCategoryDto;
import com.ecs.ecs_product.dto.SubCategoryEnriched;
import com.ecs.ecs_product.entity.ProductCategory;
import com.ecs.ecs_product.entity.SubCategory;

public class SubCategoryMapper {

    public static SubCategory mapToSubCategory(SubCategoryDto subCategoryDto) {
        if (subCategoryDto == null) {
            return null;
        }

        return new SubCategory(
                subCategoryDto.getSubCategoryId(),
                subCategoryDto.getCategoryId(),
                subCategoryDto.getSubCategoryName(),
                subCategoryDto.getSubCategoryDescription(),
                subCategoryDto.getSubCategoryImage()
        );
    }

    public static SubCategoryDto mapToSubCategoryDto(SubCategory subCategory) {
        if (subCategory == null) {
            return null;
        }

        return new SubCategoryDto(
                subCategory.getSubCategoryId(),
                subCategory.getCategoryId(),
                subCategory.getSubCategoryName(),
                subCategory.getSubCategoryDescription(),
                subCategory.getSubCategoryImage()
        );
    }

    public static SubCategoryEnriched mapToEnrichedSubCategoryDto(SubCategory subCategory, ProductCategoryDto productCategory) {
        if (subCategory == null) {
            return null;
        }
        return new SubCategoryEnriched(
                subCategory.getSubCategoryId(),
                productCategory,
                subCategory.getSubCategoryName(),
                subCategory.getSubCategoryDescription(),
                subCategory.getSubCategoryImage()
        );
    }
}