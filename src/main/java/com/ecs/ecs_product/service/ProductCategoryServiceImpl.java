package com.ecs.ecs_product.service;

import com.ecs.ecs_product.dto.ProductCategoryDto;
import com.ecs.ecs_product.entity.ProductCategory;
import com.ecs.ecs_product.exception.ResourceNotFoundException;
import com.ecs.ecs_product.mapper.ProductCategoryMapper;
import com.ecs.ecs_product.repository.ProductCategoryRepository;
import com.ecs.ecs_product.repository.ProductRepository;
import com.ecs.ecs_product.service.interfaces.IProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductCategoryServiceImpl implements IProductCategoryService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductCategoryDto getProductCategoryById(Integer categoryId) {
        ProductCategory productCategory = productCategoryRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
        return ProductCategoryMapper.mapToProductCategoryDto(productCategory);
    }

    @Override
    public List<ProductCategoryDto> getAllProductCategories() {
        List<ProductCategory> productCategories = productCategoryRepository.findAll();
        return productCategories.stream().map(ProductCategoryMapper::mapToProductCategoryDto).toList()
                .stream().filter( x -> x.getCategoryId() != 24).toList();
    }

    @Override
    public ProductCategoryDto addProductCategory(ProductCategoryDto productCategoryDto) {
        if (!productCategoryRepository.existsById(productCategoryDto.getCategoryId())) {
            ProductCategory productCategory = ProductCategoryMapper.mapToProductCategory(productCategoryDto);
            ProductCategory newProductCategory = productCategoryRepository.save(productCategory);
            return ProductCategoryMapper.mapToProductCategoryDto(newProductCategory);
        } else {
            return null;
        }
    }

    @Override
    public ProductCategoryDto updateProductCategory(ProductCategoryDto productCategoryDto) {
        ProductCategory productCategory = productCategoryRepository.findById(productCategoryDto.getCategoryId()).
                orElse(null);
        if (Objects.nonNull(productCategory)) {
            productCategory.setProductCategoryName(productCategoryDto.getCategoryName());
            ProductCategory updatedProductCategory = productCategoryRepository.save(productCategory);
            return ProductCategoryMapper.mapToProductCategoryDto(updatedProductCategory);
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteProductCategory(Integer categoryId) {
        if (categoryId != 0 && productCategoryRepository.existsById(categoryId)) {
            productRepository.deleteByProductCategoryId(categoryId);
            productCategoryRepository.deleteById(categoryId);
            return true;
        }
        return false;
    }

    @Override
    public boolean isProductCategoryExists(Integer categoryId) {
        return productCategoryRepository.existsById(categoryId);
    }
}
