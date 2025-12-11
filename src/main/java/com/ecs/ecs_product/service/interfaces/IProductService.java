package com.ecs.ecs_product.service.interfaces;

import com.ecs.ecs_product.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface IProductService {

    ProductFinalDto getProduct(Integer productId);

    List<ProductFinalDto> getAllProducts();

    Page<ProductFinalDto> getProductsByPagination(
            Pageable pageable,
            Integer categoryId,
            Integer subCategoryId,
            Integer brandId,
            String searchValue);

    Page<ProductFinalDto> getAllOutOfStockProducts(Pageable pageable);

    List<ProductFinalDto> getProductsByCategoryId(Integer categoryId);

    List<ProductFinalDto> getProductsByBrandId(Integer brandId);

    List<ProductDto> getSimilarProductsById(Integer productId);

    Object addProduct(ProductDto productDto);

    Object updateProducts(List<ProductFinalDto> productFinalDtoList);

    HttpStatus deleteProduct(Integer productId);
}
