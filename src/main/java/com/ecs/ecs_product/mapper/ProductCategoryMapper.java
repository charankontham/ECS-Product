package com.ecs.ecs_product.mapper;

import com.ecs.ecs_product.dto.ProductCategoryDto;
import com.ecs.ecs_product.entity.ProductCategory;

public class ProductCategoryMapper {
    public static ProductCategoryDto mapToProductCategoryDto(ProductCategory productCategory) {
        return new ProductCategoryDto(
                productCategory.getCategoryId(),
                productCategory.getProductCategoryName(),
                productCategory.getCategoryImage()
        );
    }

    public static ProductCategory mapToProductCategory(ProductCategoryDto productCategoryDto) {
        return new ProductCategory(
                productCategoryDto.getCategoryId(),
                productCategoryDto.getCategoryName(),
                productCategoryDto.getCategoryImage()
        );
    }
}
