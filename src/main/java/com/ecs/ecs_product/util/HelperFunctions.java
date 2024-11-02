package com.ecs.ecs_product.util;

import com.ecs.ecs_product.dto.*;
import com.ecs.ecs_product.feign.OrderService;
import com.ecs.ecs_product.service.interfaces.*;
import com.ecs.ecs_product.validations.ProductValidation;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;
import java.util.stream.Collectors;

@Setter
public class HelperFunctions {

    public static IProductService productService;

    public static List<Integer> mapToIntegerArrayList(String str) {
        return Arrays.stream(str.
                        replaceAll("[ \\[\\]]", "").
                        split(",")).
                map(Integer::parseInt).
                collect(Collectors.toList());
    }

    public static List<ProductFinalDto> getProductFinalDtoList(
            List<Integer> productIdsList
    ) {
        return productIdsList.stream().map(productService::getProduct).toList();
    }

    public static ResponseEntity<?> getResponseEntity(Object response) {
        if (Objects.equals(response, Constants.ProductNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not Found!");
        } else if (Objects.equals(response, Constants.CustomerNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer Not Found!");
        } else if (Objects.equals(response, Constants.ProductQuantityExceeded)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ProductQuantities Exceeded!");
        } else if (Objects.equals(response, Constants.AddressNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address Not Found!");
        } else if (Objects.equals(response, Constants.OrderNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order Not Found!");
        } else if (Objects.equals(response, Constants.CartNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart Not Found!");
        } else if (Objects.equals(response, Constants.UserNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!");
        } else if (Objects.equals(response, Constants.ProductCategoryNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ProductCategory Not Found!");
        } else if (Objects.equals(response, Constants.ProductBrandNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ProductBrand Not Found!");
        } else if (Objects.equals(response, Constants.ProductReviewNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ProductReview Not Found!");
        } else if (Objects.equals(response, HttpStatus.CONFLICT)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate Entry!");
        } else if (Objects.equals(response, HttpStatus.BAD_REQUEST)) {
            return new ResponseEntity<>("Validation Failed/Bad Request!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static void removeCartItemsByProductId(Integer productId, OrderService orderService) {
        List<CartDto> carts = orderService.getCartsByProductId(productId).getBody();
        for (CartDto cartDto : Objects.requireNonNull(carts)) {
            List<Integer> productIds = cartDto.getProductIds();
            List<Integer> productQuantities = cartDto.getProductQuantities();
            if (productIds.size() == 1) {
                orderService.deleteCart(cartDto.getCartId());
            } else {
                productQuantities.remove(productIds.indexOf(productId));
                productIds.remove(productId);
                cartDto.setProductIds(productIds);
                cartDto.setProductQuantities(productQuantities);
                orderService.updateCart(cartDto);
            }
        }
    }

    public static void removeOrderItemsByProductId(Integer productId, OrderService orderService) {
        List<OrderDto> orders = orderService.getAllOrdersByProductId(productId).getBody();
        for (OrderDto orderDto : Objects.requireNonNull(orders)) {
            List<Integer> productIds = orderDto.getProductIds();
            List<Integer> productQuantities = orderDto.getProductQuantities();
            if (productIds.size() == 1) {
                orderService.deleteOrder(orderDto.getOrderId());
            } else {
                productQuantities.remove(productIds.indexOf(productId));
                productIds.remove(productId);
                orderDto.setProductIds(productIds);
                orderDto.setProductQuantities(productQuantities);
                orderService.updateOrder(orderDto);
            }
        }
    }

    public static boolean getProductValidationStatus(List<ProductDto> productDtoList) {
        List<ProductDto> filteredProductDtoList = productDtoList.stream().filter(
                ProductValidation::isProductDtoSchemaValid
        ).toList();
        return filteredProductDtoList.size() == productDtoList.size();
    }

    public static boolean getProductCategoryExistsStatus(
            List<ProductDto> productDtoList,
            IProductCategoryService productCategoryService) {
        List<ProductDto> filteredProductDtoList = productDtoList.stream().
                filter(
                        (productDto) -> productCategoryService.isProductCategoryExists(
                                productDto.getProductCategoryId())).
                toList();
        return filteredProductDtoList.size() == productDtoList.size();
    }

    public static boolean getProductBrandExistsStatus(
            List<ProductDto> productDtoList,
            IProductBrandService productBrandService) {
        List<ProductDto> filteredProductDtoList = productDtoList.stream().
                filter(
                        (productDto) -> productBrandService.isProductBrandExists(
                                productDto.getProductBrandId())).
                toList();
        return filteredProductDtoList.size() == productDtoList.size();
    }
}
