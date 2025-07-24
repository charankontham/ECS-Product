package com.ecs.ecs_product.controller;

import com.ecs.ecs_product.dto.ProductBrandDto;
import com.ecs.ecs_product.service.interfaces.IProductBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/productBrand")
public class ProductBrandController {
    @Autowired
    private IProductBrandService productBrandService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductBrandDto> getProductBrandById(@PathVariable("id") Integer brandId) {
        ProductBrandDto productBrandDto = productBrandService.getProductBrandById(brandId);
        return ResponseEntity.ok(productBrandDto);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductBrandDto>> getAllProductBrands() {
        List<ProductBrandDto> productBrands = productBrandService.getAllProductBrands();
        return ResponseEntity.ok(productBrands);
    }

    @GetMapping("/getAllBrandsByPagination")
    public ResponseEntity<Page<ProductBrandDto>> getAllProductBrandsByPagination(
            @RequestParam(defaultValue = "0", name = "currentPage") Integer pageNumber,
            @RequestParam(defaultValue = "10", name = "offset") Integer itemSize,
            @RequestParam(required = false, name="searchValue") String searchValue
    ) {
        Pageable pageable = PageRequest.of(pageNumber, itemSize);
        Page<ProductBrandDto> productBrands = productBrandService.getAllProductBrandsWithPagination(pageable, searchValue);
        return ResponseEntity.ok(productBrands);
    }

    @PostMapping
    public ResponseEntity<?> addProductBrand(@RequestBody ProductBrandDto productBrandDto) {
        Object response = productBrandService.addProductBrand(productBrandDto);
        if (Objects.equals(response, HttpStatus.CONFLICT)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate entry!");
        } else if (Objects.equals(response, HttpStatus.BAD_REQUEST)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed!");
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateProductBrand(@RequestBody ProductBrandDto productBrandDto) {
        Object response = productBrandService.updateProductBrand(productBrandDto);
        if (Objects.equals(response, HttpStatus.NOT_FOUND)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product brand not found!");
        } else if (Objects.equals(response, HttpStatus.BAD_REQUEST)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed!");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductBrand(@PathVariable("id") Integer brandId) {
        boolean response = productBrandService.deleteProductBrand(brandId);
        if (response) {
            return ResponseEntity.status(HttpStatus.OK).body("Product brand deleted successfully!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product brand not found!");
    }
}
