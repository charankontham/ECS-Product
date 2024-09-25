package com.ecs.ecs_product.controller;

import com.ecs.ecs_product.dto.ProductBrandDto;
import com.ecs.ecs_product.service.interfaces.IProductBrandService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<ProductBrandDto> getProductBrandById(@PathVariable("id") int brandId) {
        ProductBrandDto productBrandDto = productBrandService.getProductBrandById(brandId);
        return ResponseEntity.ok(productBrandDto);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductBrandDto>> getAllProductBrands() {
        List<ProductBrandDto> productBrands = productBrandService.getAllProductBrands();
        return ResponseEntity.ok(productBrands);
    }

    @PostMapping
    public ResponseEntity<?> addProductBrand(@RequestBody ProductBrandDto productBrandDto) {
        Object response = productBrandService.addProductBrand(productBrandDto);
        if(Objects.equals(response, HttpStatus.CONFLICT)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Product brand already exists!");
        } else if (Objects.equals(response, HttpStatus.BAD_REQUEST)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation Failed!");
        }
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateProductBrand(@RequestBody ProductBrandDto productBrandDto) {
        Object response = productBrandService.updateProductBrand(productBrandDto);
        if(Objects.equals(response, HttpStatus.NOT_FOUND)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ProductBrand Not Found!");
        }else if (Objects.equals(response, HttpStatus.BAD_REQUEST)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation Failed!");
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductBrand(@PathVariable("id") int brandId) {
        boolean response = productBrandService.deleteProductBrand(brandId);
        if(response){
            return ResponseEntity.status(HttpStatus.OK).body("Product brand Deleted Successfully!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ProductBrand Not Found!");
    }
}
