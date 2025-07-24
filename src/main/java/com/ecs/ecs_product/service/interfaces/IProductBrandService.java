package com.ecs.ecs_product.service.interfaces;

import com.ecs.ecs_product.dto.ProductBrandDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductBrandService {

    ProductBrandDto getProductBrandById(Integer brandId);

    List<ProductBrandDto> getAllProductBrands();

    Page<ProductBrandDto> getAllProductBrandsWithPagination(Pageable pageable, String searchValue);

    Object addProductBrand(ProductBrandDto productBrandDto);

    Object updateProductBrand(ProductBrandDto productBrandDto);

    boolean deleteProductBrand(Integer brandId);

    boolean isProductBrandExists(Integer brandId);
}
