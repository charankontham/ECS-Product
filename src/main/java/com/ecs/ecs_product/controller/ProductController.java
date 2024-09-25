package com.ecs.ecs_product.controller;

import com.ecs.ecs_product.dto.ProductDto;
import com.ecs.ecs_product.dto.ProductFinalDto;
import com.ecs.ecs_product.service.interfaces.IProductService;
import com.ecs.ecs_product.util.Constants;
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
    public ResponseEntity<ProductFinalDto> getProductById(@PathVariable("id") int productId) {
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

    @PutMapping()
    public ResponseEntity<?> updateProduct(@RequestBody ProductFinalDto productFinalDto) {
        Object response = productService.updateProduct(productFinalDto);
        return HelperFunctions.getResponseEntity(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") int productId) {
        boolean isDeleted = productService.deleteProduct(productId);
        if(isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully!");
        }
        return HelperFunctions.getResponseEntity(Constants.ProductNotFound);
    }
}