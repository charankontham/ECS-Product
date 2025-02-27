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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private IProductCategoryService productCategoryService;
    @Autowired
    private ISubCategoryService subCategoryService;
    @Autowired
    private IProductBrandService productBrandService;
    @Autowired
    private CheckDependencies checkDependencies;

    @Override
    public ProductFinalDto getProduct(Integer productId) {
        Product product = productRepository.findById(productId).
                orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        return ProductMapper.mapToProductFinalDto(product, subCategoryService, productBrandService);
    }

    @Override
    public List<ProductFinalDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map((product) -> ProductMapper.
                        mapToProductFinalDto(
                                product,
                                subCategoryService,
                                productBrandService)).toList()
                .stream().filter(x -> x.getProductId() != 9).toList();
    }

    @Override
    public List<ProductFinalDto> getProductsByCategoryId(Integer categoryId) {
        List<Product> products = productRepository.getProductsByProductCategoryId(categoryId);
        return products.stream().map((product) -> ProductMapper.
                        mapToProductFinalDto(
                                product,
                                subCategoryService,
                                productBrandService)).toList()
                .stream().filter(x -> x.getProductId() != 9).toList();
    }

    @Override
    public List<ProductDto> getSimilarProductsById(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        final int TARGET_SIZE = 10;
        final double PRICE_THRESHOLD = product.getProductPrice() * 0.2;
        double minPrice = product.getProductPrice() - PRICE_THRESHOLD;
        double maxPrice = product.getProductPrice() + PRICE_THRESHOLD;
        /* Filtering all the product category items **/
        List<Product> similarProducts = productRepository.findByProductCategoryIdAndProductIdNot(
                product.getProductCategoryId(), productId);
        System.out.println("similar products after category matching: ");
        System.out.println(similarProducts.stream().map(Object::toString).toList());

        /* Filter products with sub category or price range of +-50 or brand **/
        if (similarProducts.size() > TARGET_SIZE) {
            similarProducts = similarProducts.stream()
                    .filter(
                            (currProduct) ->
                                    (currProduct.getProductPrice() >= minPrice &&
                                            currProduct.getProductPrice() <= maxPrice) ||
                                            (Objects.equals(currProduct.getProductBrandId(),
                                                    product.getProductBrandId()) ||
                                                    (Objects.equals(currProduct.getSubCategoryId(),
                                                            product.getSubCategoryId())))).toList();
        }
        /* Filter products with sub category and brand only **/
        if(similarProducts.size() > TARGET_SIZE) {
            similarProducts = similarProducts.stream()
                    .filter((currProduct) ->
                            (Objects.equals(currProduct.getProductBrandId(),
                                    product.getProductBrandId()) ||
                                    (Objects.equals(currProduct.getSubCategoryId(),
                                            product.getSubCategoryId())))).toList();
        }
        /* Filter products with same price range only **/
        if(similarProducts.size() > TARGET_SIZE) {
            similarProducts = similarProducts.stream()
                    .filter((currProduct) ->
                            (currProduct.getProductPrice() < maxPrice &&
                                    currProduct.getProductPrice() > minPrice)).toList();
        }
//        similarProducts = similarProducts.stream()
//                .filter(p -> Objects.equals(p.getProductBrandId(), product.getProductBrandId()) ||
//                        Math.abs(p.getProductPrice() - product.getProductPrice()) <= product.getProductPrice() * 0.2)
//                .toList();
//        System.out.println("similar products after brand or price matching: ");
//        System.out.println(similarProducts.stream().map(Object::toString).toList());
        return similarProducts.stream().map(ProductMapper::mapToProductDto).
                toList().
                subList(0, Math.min(similarProducts.size(), 10));
    }

    @Override
    public Object addProduct(ProductDto productDto) {
        boolean productIdExists = Objects.nonNull(productDto.getProductId());
        if (productIdExists) {
            if (productRepository.existsById(productDto.getProductId())) {
                return HttpStatus.CONFLICT;
            }
        }
        return validateAndSaveOrUpdateProduct(List.of(productDto));
    }

    @Override
    public Object updateProducts(List<ProductFinalDto> productFinalDtoList) {
        List<ProductFinalDto> productExistsList = productFinalDtoList.stream().filter(
                productFinalDto -> productRepository.existsById(productFinalDto.getProductId())
        ).toList();
        if (productExistsList.size() == productFinalDtoList.size()) {
            List<ProductDto> productDtoList = productFinalDtoList.
                    stream().map(ProductMapper::mapToProductDto).toList();
            return validateAndSaveOrUpdateProduct(productDtoList);
        }
        return Constants.ProductNotFound;
    }

    @Override
    public HttpStatus deleteProduct(Integer productId) {
        if (productId != 0 && productRepository.existsById(productId)) {
            if (!checkDependencies.productDependenciesExists(productId)) {
                productRepository.deleteById(productId);
                return HttpStatus.OK;
            }
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.NOT_FOUND;
    }

    private Object validateAndSaveOrUpdateProduct(List<ProductDto> productDtoList) {
        if (!HelperFunctions.getProductValidationStatus(productDtoList)) {
            return HttpStatus.BAD_REQUEST;
        } else if (!HelperFunctions.getProductBrandExistsStatus(productDtoList, productBrandService)) {
            return Constants.ProductBrandNotFound;
        } else if (!HelperFunctions.getProductCategoryExistsStatus(productDtoList, productCategoryService)) {
            return Constants.ProductCategoryNotFound;
        } else {
            List<Product> products = productRepository.
                    saveAll(productDtoList.stream().map(ProductMapper::mapToProduct).collect(toList()));
            return products.stream().map((product) ->
                    ProductMapper.mapToProductFinalDto(product, subCategoryService, productBrandService)).toList();
        }
    }
}
