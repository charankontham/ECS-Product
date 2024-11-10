package com.ecs.ecs_product.service.interfaces;

import com.ecs.ecs_product.dto.ProductCategoryDto;

import java.util.List;

public interface IProductCategoryService {

    ProductCategoryDto getProductCategoryById(Integer categoryId);

    List<ProductCategoryDto> getAllProductCategories();

    ProductCategoryDto addProductCategory(ProductCategoryDto productCategoryDto);

    ProductCategoryDto updateProductCategory(ProductCategoryDto productCategoryDto);

    boolean deleteProductCategory(Integer categoryId);

    boolean isProductCategoryExists(Integer categoryId);
}
