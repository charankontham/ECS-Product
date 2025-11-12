package com.ecs.ecs_product.controller;

import com.ecs.ecs_product.dto.ProductDto;
import com.ecs.ecs_product.dto.ProductFinalDto;
import com.ecs.ecs_product.service.interfaces.IProductService;
import com.ecs.ecs_product.util.HelperFunctions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ProductController {
    private final IProductService productService;

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

    @GetMapping("/getProductsByPagination")
    public ResponseEntity<Page<ProductFinalDto>> getProductsByPagination(
            @RequestParam(defaultValue = "0", name = "currentPage") Integer pageNumber,
            @RequestParam(defaultValue = "10", name = "offset") Integer itemSize,
            @RequestParam(required = false, name="categoryId") Integer categoryId,
            @RequestParam(required = false, name="subCategoryId") Integer subCategoryId,
            @RequestParam(required = false, name="brandId") Integer brandId,
            @RequestParam(required = false, name="searchValue") String searchValue) {
        Pageable pageable = PageRequest.of(pageNumber, itemSize);
        Page<ProductFinalDto> products = productService.getProductsByPagination(pageable, categoryId, subCategoryId, brandId, searchValue);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/getAllOutOfStockProductsByPagination")
    public ResponseEntity<Page<ProductFinalDto>> getAllOutOfStockProducts(
            @RequestParam(defaultValue = "0", name="currentPage") Integer pageNumber,
            @RequestParam(defaultValue = "5", name = "offset") Integer offset
    ){
        Pageable pageable = PageRequest.of(pageNumber, offset);
        Page<ProductFinalDto> products = productService.getAllOutOfStockProducts(pageable);
        return ResponseEntity.ok(products);
    }
    @GetMapping("/getProductsByCategoryId/{id}")
    public ResponseEntity<List<ProductFinalDto>> getAllProductsByCategoryId(@PathVariable("id") Integer categoryId) {
        List<ProductFinalDto> products = productService.getProductsByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/getProductsByBrandId/{id}")
    public ResponseEntity<List<ProductFinalDto>> getAllProductsByBrandId(@PathVariable("id") Integer brandId) {
        List<ProductFinalDto> products = productService.getProductsByBrandId(brandId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/getSimilarProductsById/{id}")
    public ResponseEntity<List<ProductDto>> getSimilarProducts(@PathVariable("id") Integer productId) {
        List<ProductDto> products = productService.getSimilarProductsById(productId);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {
        Object response = productService.addProduct(productDto);
        if(response instanceof List && !((List<?>) response).isEmpty()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(((List<?>)response).get(0));
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
