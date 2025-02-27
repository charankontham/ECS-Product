package com.ecs.ecs_product.service.interfaces;

import com.ecs.ecs_product.dto.ProductDto;
import com.ecs.ecs_product.dto.ProductFinalDto;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface IProductService {

    ProductFinalDto getProduct(Integer productId);

    List<ProductFinalDto> getAllProducts();

    List<ProductFinalDto> getProductsByCategoryId(Integer categoryId);

    List<ProductDto> getSimilarProductsById(Integer productId);

    Object addProduct(ProductDto productDto);

    Object updateProducts(List<ProductFinalDto> productFinalDtoList);

    HttpStatus deleteProduct(Integer productId);
}
