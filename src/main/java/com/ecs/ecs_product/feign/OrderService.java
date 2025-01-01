package com.ecs.ecs_product.feign;

import com.ecs.ecs_product.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ECS-ORDER", configuration = FeignClientConfig.class)
public interface OrderService {
    @GetMapping("/api/order/existsByProductId/{id}")
    ResponseEntity<Boolean> ordersExistsByProductId(@PathVariable("id") Integer productId);

    @GetMapping("api/cart/existsByProductId/{id}")
    ResponseEntity<Boolean> cartsExistsByProductId(@PathVariable("id") Integer productId);
}
