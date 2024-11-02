package com.ecs.ecs_product.service;

import com.ecs.ecs_product.feign.OrderService;
import com.ecs.ecs_product.feign.ProductReviewService;
import com.ecs.ecs_product.util.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoveDependencies {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductReviewService productReviewService;

    public void deleteProductDependencies(Integer productId) {
        productReviewService.deleteProductReviewByProductId(productId);
        HelperFunctions.removeCartItemsByProductId(productId, orderService);
        HelperFunctions.removeOrderItemsByProductId(productId, orderService);
    }
}
