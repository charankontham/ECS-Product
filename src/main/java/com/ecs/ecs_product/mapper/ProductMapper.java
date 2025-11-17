package com.ecs.ecs_product.mapper;

import com.ecs.ecs_product.dto.*;
import com.ecs.ecs_product.entity.Product;
import com.ecs.ecs_product.service.interfaces.IProductBrandService;
import com.ecs.ecs_product.service.interfaces.IProductCategoryService;
import com.ecs.ecs_product.service.interfaces.ISubCategoryService;

public class ProductMapper {

    public static Product mapToProduct(ProductDto productDto) {
        return new Product(
                productDto.getProductId(),
                productDto.getProductCategoryId(),
                productDto.getSubCategoryId(),
                productDto.getProductBrandId(),
                productDto.getProductName(),
                productDto.getProductDescription(),
                productDto.getProductPrice(),
                productDto.getProductQuantity(),
                productDto.getProductImage(),
                productDto.getProductColor(),
                productDto.getProductWeight(),
                productDto.getDateAdded(),
                productDto.getDateModified(),
                productDto.getProductDimensions(),
                productDto.getProductCondition()
        );
    }

    public static ProductDto mapToProductDto(Product product) {
        return new ProductDto(
                product.getProductId(),
                product.getProductCategoryId(),
                product.getSubCategoryId(),
                product.getProductBrandId(),
                product.getProductName(),
                product.getProductDescription(),
                product.getProductPrice(),
                product.getProductQuantity(),
                product.getProductImage(),
                product.getProductColor(),
                product.getProductWeight(),
                product.getDateAdded(),
                product.getDateModified(),
                product.getProductDimensions(),
                product.getProductCondition()
        );
    }

    public static ProductFinalDto mapToProductFinalDto(
            Product product,
            ISubCategoryService subCategoryService,
            IProductBrandService productBrandService
    ) {
        SubCategoryEnriched subCategory = subCategoryService.getEnrichedSubCategoryById(product.getSubCategoryId());
        ProductBrandDto productBrandDto = productBrandService.getProductBrandById(product.getProductBrandId());
        return new ProductFinalDto(
                product.getProductId(),
                product.getProductName(),
                productBrandDto,
                subCategory,
                product.getProductDescription(),
                product.getProductPrice(),
                product.getProductQuantity(),
                product.getProductImage(),
                product.getProductColor(),
                product.getProductWeight(),
                product.getDateAdded(),
                product.getDateModified(),
                product.getProductDimensions(),
                product.getProductCondition()
        );
    }

    public static ProductFinalDto mapToProductFinalDto(
            ProductWithRelevanceProjection product,
            ISubCategoryService subCategoryService,
            IProductBrandService productBrandService
    ) {
        SubCategoryEnriched subCategory = subCategoryService.getEnrichedSubCategoryById(product.getSubCategoryId());
        ProductBrandDto productBrandDto = productBrandService.getProductBrandById(product.getProductBrandId());
        return new ProductFinalDto(
                product.getProductId(),
                product.getProductName(),
                productBrandDto,
                subCategory,
                product.getProductDescription(),
                product.getProductPrice(),
                product.getProductQuantity(),
                product.getProductImage(),
                product.getProductColor(),
                product.getProductWeight(),
                product.getDateAdded(),
                product.getDateModified(),
                product.getProductDimensions(),
                product.getProductCondition()
        );
    }

    public static ProductDto mapToProductDto(ProductFinalDto productFinalDto) {
        return new ProductDto(
                productFinalDto.getProductId(),
                productFinalDto.getProductSubCategory().getProductCategory().getCategoryId(),
                productFinalDto.getProductSubCategory().getSubCategoryId(),
                productFinalDto.getBrand().getBrandId(),
                productFinalDto.getProductName(),
                productFinalDto.getProductDescription(),
                productFinalDto.getProductPrice(),
                productFinalDto.getProductQuantity(),
                productFinalDto.getProductImage(),
                productFinalDto.getProductColor(),
                productFinalDto.getProductWeight(),
                productFinalDto.getDateAdded(),
                productFinalDto.getDateModified(),
                productFinalDto.getProductDimensions(),
                productFinalDto.getProductCondition()
        );
    }
}
