package com.ecs.ecs_product.util;

import com.ecs.ecs_product.dto.*;
import com.ecs.ecs_product.service.interfaces.*;
import com.ecs.ecs_product.validations.ProductValidation;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

@Setter
public class HelperFunctions {

    public static ResponseEntity<?> getResponseEntity(Object response) {
        if (Objects.equals(response, Constants.ProductNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found!");
        } else if (Objects.equals(response, Constants.CustomerNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found!");
        } else if (Objects.equals(response, Constants.ProductQuantityExceeded)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product quantities exceeded!");
        } else if (Objects.equals(response, Constants.AddressNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address not found!");
        } else if (Objects.equals(response, Constants.OrderNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found!");
        } else if (Objects.equals(response, Constants.CartNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found!");
        } else if (Objects.equals(response, Constants.UserNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        } else if (Objects.equals(response, Constants.ProductCategoryNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product category not found!");
        } else if (Objects.equals(response, Constants.ProductBrandNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product brand not found!");
        } else if (Objects.equals(response, Constants.ProductReviewNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product review not found!");
        } else if (Objects.equals(response, HttpStatus.CONFLICT)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate entry!");
        } else if (Objects.equals(response, HttpStatus.BAD_REQUEST)) {
            return new ResponseEntity<>("Validation failed/Bad request!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static boolean getProductValidationStatus(List<ProductDto> productDtoList) {
        List<ProductDto> filteredProductDtoList = productDtoList.stream().filter(
                ProductValidation::isProductDtoSchemaValid).toList();
        return filteredProductDtoList.size() == productDtoList.size();
    }

    public static boolean getProductCategoryExistsStatus(
            List<ProductDto> productDtoList,
            IProductCategoryService productCategoryService) {
        List<ProductDto> filteredProductDtoList = productDtoList.stream().
                filter((productDto) -> productCategoryService.isProductCategoryExists(
                        productDto.getProductCategoryId())).toList();
        return filteredProductDtoList.size() == productDtoList.size();
    }

    public static boolean getProductBrandExistsStatus(
            List<ProductDto> productDtoList,
            IProductBrandService productBrandService) {
        List<ProductDto> filteredProductDtoList = productDtoList.stream().
                filter((productDto) -> productBrandService.isProductBrandExists(
                        productDto.getProductBrandId())).toList();
        return filteredProductDtoList.size() == productDtoList.size();
    }
}
