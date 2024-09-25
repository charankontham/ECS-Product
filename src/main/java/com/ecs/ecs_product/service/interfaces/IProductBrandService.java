package com.ecs.ecs_product.service.interfaces;

import com.ecs.ecs_product.dto.ProductBrandDto;
import java.util.List;

public interface IProductBrandService {

    ProductBrandDto getProductBrandById(int brandId);

    List<ProductBrandDto> getAllProductBrands();

    Object addProductBrand(ProductBrandDto productBrandDto);

    Object updateProductBrand(ProductBrandDto productBrandDto);

    boolean deleteProductBrand(int brandId);

    boolean isProductBrandExists(int brandId);


}
