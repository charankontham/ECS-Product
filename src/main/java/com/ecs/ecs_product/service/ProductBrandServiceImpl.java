package com.ecs.ecs_product.service;

import com.ecs.ecs_product.dto.ProductBrandDto;
import com.ecs.ecs_product.entity.ProductBrand;
import com.ecs.ecs_product.exception.ResourceNotFoundException;
import com.ecs.ecs_product.mapper.ProductBrandMapper;
import com.ecs.ecs_product.repository.ProductBrandRepository;
import com.ecs.ecs_product.service.interfaces.IProductBrandService;
import com.ecs.ecs_product.validations.BasicValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductBrandServiceImpl implements IProductBrandService {
    @Autowired
    private ProductBrandRepository productBrandRepository;

    @Override
    public ProductBrandDto getProductBrandById(Integer brandId) {
        ProductBrand productBrand = productBrandRepository.findById(brandId).
                orElseThrow(() -> new ResourceNotFoundException("Product brand not found!"));
        return ProductBrandMapper.mapToProductBrandDto(productBrand);
    }

    @Override
    public List<ProductBrandDto> getAllProductBrands() {
        List<ProductBrand> productBrands = productBrandRepository.findAll();
        return productBrands.stream().map(ProductBrandMapper::mapToProductBrandDto).
                collect(Collectors.toList());
    }

    @Override
    public Object addProductBrand(ProductBrandDto productBrandDto) {
        boolean productBrandExists = productBrandDto.getBrandId() != null && productBrandRepository.existsById(productBrandDto.getBrandId());
        if (!BasicValidation.stringValidation(productBrandDto.getBrandName())) {
            return HttpStatus.BAD_REQUEST;
        }
        if (!productBrandExists) {
            ProductBrand productBrand = productBrandRepository.save(ProductBrandMapper.mapToProductBrand(productBrandDto));
            return ProductBrandMapper.mapToProductBrandDto(productBrand);
        }
        return HttpStatus.CONFLICT;
    }

    @Override
    public Object updateProductBrand(ProductBrandDto productBrandDto) {
        boolean productBrandExists = productBrandRepository.existsById(productBrandDto.getBrandId());
        if (!BasicValidation.stringValidation(productBrandDto.getBrandName())) {
            return HttpStatus.BAD_REQUEST;
        }
        if (productBrandExists) {
            ProductBrand productBrand = productBrandRepository.save(ProductBrandMapper.mapToProductBrand(productBrandDto));
            return ProductBrandMapper.mapToProductBrandDto(productBrand);
        }
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public boolean deleteProductBrand(Integer brandId) {
        boolean isDeleted = productBrandRepository.existsById(brandId);
        if (isDeleted) {
            productBrandRepository.deleteById(brandId);
            return true;
        }
        return false;
    }

    @Override
    public boolean isProductBrandExists(Integer brandId) {
        return productBrandRepository.existsById(brandId);
    }
}
