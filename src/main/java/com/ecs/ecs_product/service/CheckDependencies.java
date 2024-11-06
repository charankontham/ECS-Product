package com.ecs.ecs_product.service;

import com.ecs.ecs_product.dto.ProductReviewDto;
import com.ecs.ecs_product.feign.OrderService;
import com.ecs.ecs_product.feign.ProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CheckDependencies {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductReviewService productReviewService;

    public boolean productDependenciesExists(Integer productId) {
        if (!Objects.requireNonNull(productReviewService.getProductReviewsByProductId(productId).getBody()).isEmpty()) {
            return true;
        } else if (Boolean.TRUE.equals(orderService.cartsExistsByProductId(productId).getBody())){
            return true;
        } else return Boolean.TRUE.equals(orderService.ordersExistsByProductId(productId).getBody());
    }
}
