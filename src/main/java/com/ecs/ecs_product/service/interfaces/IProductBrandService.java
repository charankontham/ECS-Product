package com.ecs.ecs_product.service.interfaces;

import com.ecs.ecs_product.dto.ProductBrandDto;

import java.util.List;

public interface IProductBrandService {

    ProductBrandDto getProductBrandById(Integer brandId);

    List<ProductBrandDto> getAllProductBrands();

    Object addProductBrand(ProductBrandDto productBrandDto);

    Object updateProductBrand(ProductBrandDto productBrandDto);

    boolean deleteProductBrand(Integer brandId);

    boolean isProductBrandExists(Integer brandId);
}
