package com.ecs.ecs_product.validations;

import com.ecs.ecs_product.dto.ProductDto;

import java.util.Objects;

public class ProductValidation {

    public static boolean isProductDtoSchemaValid(ProductDto productDto) {
        return Objects.nonNull(productDto) &&
                Objects.nonNull(productDto.getProductCategoryId()) &&
                productDto.getProductCategoryId()!=0 &&
                Objects.nonNull(productDto.getProductBrandId()) &&
                productDto.getProductBrandId()!=0 &&
                Objects.nonNull(productDto.getProductCondition()) &&
                BasicValidation.stringValidation(productDto.getProductName());
    }
}
