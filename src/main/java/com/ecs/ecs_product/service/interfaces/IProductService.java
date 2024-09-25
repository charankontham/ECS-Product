package com.ecs.ecs_product.service.interfaces;

import com.ecs.ecs_product.dto.ProductDto;
import com.ecs.ecs_product.dto.ProductFinalDto;
import java.util.List;

public interface IProductService {

    ProductFinalDto getProduct(Integer productId);

    List<ProductFinalDto> getAllProducts();

    Object addProduct(ProductDto productDto);

    Object updateProduct(ProductFinalDto productFinalDto);

    boolean deleteProduct(Integer productId);

    boolean isProductExists(Integer productId);
}
