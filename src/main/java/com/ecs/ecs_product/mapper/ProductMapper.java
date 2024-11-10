package com.ecs.ecs_product.mapper;

import com.ecs.ecs_product.dto.ProductBrandDto;
import com.ecs.ecs_product.dto.ProductCategoryDto;
import com.ecs.ecs_product.dto.ProductDto;
import com.ecs.ecs_product.dto.ProductFinalDto;
import com.ecs.ecs_product.entity.Product;
import com.ecs.ecs_product.service.interfaces.IProductBrandService;
import com.ecs.ecs_product.service.interfaces.IProductCategoryService;

public class ProductMapper {

    public static Product mapToProduct(ProductDto productDto) {
        return new Product(
                productDto.getProductId(),
                productDto.getProductCategoryId(),
                productDto.getProductBrandId(),
                productDto.getProductName(),
                productDto.getProductDescription(),
                productDto.getProductPrice(),
                productDto.getProductQuantity(),
                productDto.getProductColor(),
                productDto.getProductWeight(),
                productDto.getProductDimensions(),
                productDto.getProductCondition()
        );
    }

    public static ProductFinalDto mapToProductFinalDto(
            Product product,
            IProductCategoryService productCategoryService,
            IProductBrandService productBrandService
    ) {
        ProductCategoryDto productCategoryDto = productCategoryService.getProductCategoryById(product.getProductCategoryId());
        ProductBrandDto productBrandDto = productBrandService.getProductBrandById(product.getProductBrandId());
        return new ProductFinalDto(
                product.getProductId(),
                product.getProductName(),
                productBrandDto,
                productCategoryDto,
                product.getProductDescription(),
                product.getProductPrice(),
                product.getProductQuantity(),
                product.getProductColor(),
                product.getProductWeight(),
                product.getProductDimensions(),
                product.getProductCondition()
        );
    }

    public static ProductDto mapToProductDto(ProductFinalDto productFinalDto) {
        return new ProductDto(
                productFinalDto.getProductId(),
                productFinalDto.getProductCategory().getCategoryId(),
                productFinalDto.getBrand().getBrandId(),
                productFinalDto.getProductName(),
                productFinalDto.getProductDescription(),
                productFinalDto.getProductPrice(),
                productFinalDto.getProductQuantity(),
                productFinalDto.getProductColor(),
                productFinalDto.getProductWeight(),
                productFinalDto.getProductDimensions(),
                productFinalDto.getProductCondition()
        );
    }
}
