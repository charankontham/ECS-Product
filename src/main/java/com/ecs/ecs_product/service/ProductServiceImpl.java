package com.ecs.ecs_product.service;

import com.ecs.ecs_product.dto.ProductDto;
import com.ecs.ecs_product.dto.ProductFinalDto;
import com.ecs.ecs_product.entity.Product;
import com.ecs.ecs_product.exception.ResourceNotFoundException;
import com.ecs.ecs_product.mapper.ProductMapper;
import com.ecs.ecs_product.repository.ProductRepository;
import com.ecs.ecs_product.service.interfaces.*;
import com.ecs.ecs_product.util.Constants;
import com.ecs.ecs_product.validations.ProductValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private IProductCategoryService productCategoryService;
    @Autowired
    private IProductBrandService productBrandService;
    @Autowired
    private RemoveDependencies removeDependencies;

    @Override
    public ProductFinalDto getProduct(Integer productId) {
        Product product = productRepository.findById(productId).
                orElseThrow(() -> new ResourceNotFoundException("Product Not Found!"));
        return ProductMapper.mapToProductFinalDto(product, productCategoryService, productBrandService);
    }

    @Override
    public List<ProductFinalDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map((product) -> ProductMapper.
                mapToProductFinalDto(
                        product,
                        productCategoryService,
                        productBrandService)
                ).
                collect(Collectors.toList());
    }

    @Override
    public Object addProduct(ProductDto productDto) {
        boolean productExists = productRepository.existsById(productDto.getProductId());
        if(productExists) {
            return HttpStatus.CONFLICT;
        }
        return validateAndSaveOrUpdateProduct(productDto);
    }

    @Override
    public Object updateProduct(ProductFinalDto productFinalDto) {
        boolean productExists = productRepository.existsById(productFinalDto.getProductId());
        if(productExists) {
            return validateAndSaveOrUpdateProduct(ProductMapper.mapToProductDto(productFinalDto));
        }
        return Constants.ProductNotFound;
    }

    @Override
    public boolean deleteProduct(Integer productId) {
        if(productId!=0 && productRepository.existsById(productId)){
            removeDependencies.deleteProductDependencies(productId);
            productRepository.deleteById(productId);
            return true;
        }
        return false;
    }

    @Override
    public boolean isProductExists(Integer productId) {
        return productRepository.existsById(productId);
    }

    private Object validateAndSaveOrUpdateProduct(ProductDto productDto) {
        if(!ProductValidation.isProductDtoSchemaValid(productDto)){
            return HttpStatus.BAD_REQUEST;
        }
        boolean productCategoryExists = productCategoryService.
                isProductCategoryExists(productDto.getProductCategoryId());
        boolean productBrandExists = productBrandService.isProductBrandExists(productDto.getProductBrandId());
        if(!productBrandExists){
            return Constants.ProductBrandNotFound;
        } else if (!productCategoryExists) {
            return Constants.ProductCategoryNotFound;
        } else {
            Product product = productRepository.
                    save(ProductMapper.mapToProduct(productDto));
            return ProductMapper.mapToProductFinalDto(product, productCategoryService, productBrandService);
        }
    }
}
