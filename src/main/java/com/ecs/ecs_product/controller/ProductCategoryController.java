package com.ecs.ecs_product.controller;

import com.ecs.ecs_product.dto.ProductCategoryDto;
import com.ecs.ecs_product.service.interfaces.IProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/productCategory")
public class ProductCategoryController {
    @Autowired
    private IProductCategoryService productCategoryService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductCategoryById(@PathVariable("id") Integer categoryId) {
        ProductCategoryDto productCategoryDto = productCategoryService.getProductCategoryById(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(productCategoryDto);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllProductCategory() {
        List<ProductCategoryDto> productCategories = productCategoryService.getAllProductCategories();
        return ResponseEntity.ok(productCategories);
    }

    @PostMapping
    public ResponseEntity<?> addProductCategory(@RequestBody ProductCategoryDto productCategoryDto) {
        ProductCategoryDto newProductCategoryDto = productCategoryService.addProductCategory(productCategoryDto);
        if (!Objects.nonNull(newProductCategoryDto)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Incorrect request body!");
        }
        return new ResponseEntity<>(newProductCategoryDto, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<?> updateProductCategory(@RequestBody ProductCategoryDto productCategoryDto) {
        ProductCategoryDto newProductCategoryDto = productCategoryService.updateProductCategory(productCategoryDto);
        if (!Objects.nonNull(newProductCategoryDto)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CategoryId not found!");
        }
        return new ResponseEntity<>(newProductCategoryDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductCategory(@PathVariable("id") Integer productCategoryId) {
        boolean isDeleted = productCategoryService.deleteProductCategory(productCategoryId);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Product category deleted successfully!");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product category not found!");
    }
}
