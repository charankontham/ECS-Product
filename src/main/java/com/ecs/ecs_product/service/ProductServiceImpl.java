package com.ecs.ecs_product.service;

import com.ecs.ecs_product.dto.ProductDto;
import com.ecs.ecs_product.dto.ProductFinalDto;
import com.ecs.ecs_product.entity.Product;
import com.ecs.ecs_product.exception.ResourceNotFoundException;
import com.ecs.ecs_product.mapper.ProductMapper;
import com.ecs.ecs_product.repository.ProductRepository;
import com.ecs.ecs_product.service.interfaces.*;
import com.ecs.ecs_product.util.Constants;
import com.ecs.ecs_product.util.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

        boolean productIdExists = Objects.nonNull(productDto.getProductId());
        if(productIdExists) {
            if(productRepository.existsById(productDto.getProductId())){
                return HttpStatus.CONFLICT;
            }
        }
        return validateAndSaveOrUpdateProduct(List.of(productDto));
    }

//    @Override
//    public Object updateProducts(ProductFinalDto productFinalDto) {
//        boolean productExists = productRepository.existsById(productFinalDto.getProductId());
//        if(productExists) {
//            return validateAndSaveOrUpdateProduct(ProductMapper.mapToProductDto(productFinalDto));
//        }
//        return Constants.ProductNotFound;
//    }

    @Override
    public Object updateProducts(List<ProductFinalDto> productFinalDtoList) {
        List<ProductFinalDto> productExistsList = productFinalDtoList.stream().filter(
                productFinalDto -> productRepository.existsById(productFinalDto.getProductId())
        ).toList();
        if(productExistsList.size() == productFinalDtoList.size()) {
            List<ProductDto> productDtoList = productFinalDtoList.
                    stream().map(ProductMapper::mapToProductDto).toList();
            return validateAndSaveOrUpdateProduct(productDtoList);
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

    private Object validateAndSaveOrUpdateProduct(List<ProductDto> productDtoList) {
        if(!HelperFunctions.getProductValidationStatus(productDtoList)){
            return HttpStatus.BAD_REQUEST;
        } else if(!HelperFunctions.getProductBrandExistsStatus(productDtoList, productBrandService)){
            return Constants.ProductBrandNotFound;
        } else if (!HelperFunctions.getProductCategoryExistsStatus(productDtoList, productCategoryService)) {
            return Constants.ProductCategoryNotFound;
        } else {
            List<Product> products = productRepository.
                    saveAll(productDtoList.stream().map(ProductMapper::mapToProduct).collect(Collectors.toList()));
            return products.stream().map((product) -> ProductMapper.mapToProductFinalDto(product, productCategoryService, productBrandService) ).toList();
        }
    }
}
