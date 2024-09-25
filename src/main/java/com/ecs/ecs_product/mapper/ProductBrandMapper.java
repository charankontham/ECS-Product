package com.ecs.ecs_product.mapper;

import com.ecs.ecs_product.dto.ProductBrandDto;
import com.ecs.ecs_product.entity.ProductBrand;

public class ProductBrandMapper {
    public static ProductBrand mapToProductBrand(ProductBrandDto productBrandDto) {
        return new ProductBrand(
                productBrandDto.getBrandId(),
                productBrandDto.getBrandName(),
                productBrandDto.getBrandDescription()
        );
    }

    public static ProductBrandDto mapToProductBrandDto(ProductBrand productBrand) {
        return new ProductBrandDto(
                productBrand.getBrandId(),
                productBrand.getBrandName(),
                productBrand.getBrandDescription()
        );
    }
}
