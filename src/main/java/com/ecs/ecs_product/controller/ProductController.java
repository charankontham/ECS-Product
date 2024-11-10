package com.ecs.ecs_product.controller;

import com.ecs.ecs_product.dto.ProductDto;
import com.ecs.ecs_product.dto.ProductFinalDto;
import com.ecs.ecs_product.service.interfaces.IProductService;
import com.ecs.ecs_product.util.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductFinalDto> getProductById(@PathVariable("id") Integer productId) {
        ProductFinalDto productFinalDto = productService.getProduct(productId);
        return ResponseEntity.ok(productFinalDto);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductFinalDto>> getAllProducts() {
        List<ProductFinalDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {
        Object response = productService.addProduct(productDto);
        if(response instanceof ProductFinalDto) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        return HelperFunctions.getResponseEntity(response);
    }

    @PutMapping
    public ResponseEntity<?> updateProducts(@RequestBody List<ProductFinalDto> productFinalDtoList) {
        Object response = productService.updateProducts(productFinalDtoList);
        return HelperFunctions.getResponseEntity(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Integer productId) {
        HttpStatus status = productService.deleteProduct(productId);
        if(status.equals(HttpStatus.OK)) {
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully!");
        }else if(status.equals(HttpStatus.NOT_FOUND)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found!");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Product cannot be deleted!");
    }
}
