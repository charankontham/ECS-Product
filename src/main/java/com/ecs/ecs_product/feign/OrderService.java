package com.ecs.ecs_product.feign;

import com.ecs.ecs_product.dto.CartDto;
import com.ecs.ecs_product.dto.OrderDto;
import com.ecs.ecs_product.dto.OrderFinalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name="ECS-ORDER", configuration = FeignClientConfig.class)
public interface OrderService {

    @GetMapping("/api/order/{id}")
    ResponseEntity<OrderFinalDto> getOrderById(@PathVariable("id") Integer orderId);

    @GetMapping("/api/order/")
    ResponseEntity<List<OrderFinalDto>> getAllOrders();

    @GetMapping("/api/order/getOrdersByCustomerId/{id}")
    ResponseEntity<List<OrderFinalDto>> getAllOrdersByCustomerId(@PathVariable("id") Integer customerId);

    @GetMapping("/api/order/getOrdersByProductId/{id}")
    ResponseEntity<List<OrderDto>> getAllOrdersByProductId(@PathVariable("id") Integer productId);

    @GetMapping("/api/order/existsByProductId/{id}")
    ResponseEntity<Boolean> ordersExistsByProductId(@PathVariable("id") Integer productId);

    @PostMapping("/api/order")
    ResponseEntity<?> addOrder(@RequestBody OrderDto orderDto);

    @PutMapping("/api/order")
    ResponseEntity<?> updateOrder(@RequestBody OrderDto orderDto);

    @DeleteMapping("/api/order/{id}")
    ResponseEntity<String> deleteOrder(@PathVariable("id") Integer orderId);

    @GetMapping("/api/cart/{id}")
    ResponseEntity<?> getCart(@PathVariable("id") Integer cartId);

    @GetMapping("/api/cart/getCartByCustomerId/{id}")
    ResponseEntity<?> getCartByCustomerId(@PathVariable("id") Integer customerId);

    @GetMapping("api/cart/getCartsByProductId/{id}")
    ResponseEntity<List<CartDto>> getCartsByProductId(@PathVariable("id") Integer productId);

    @GetMapping("api/cart/existsByProductId/{id}")
    ResponseEntity<Boolean> cartsExistsByProductId(@PathVariable("id") Integer productId);

    @PostMapping("/api/cart")
    ResponseEntity<?> addCart(@RequestBody CartDto cartDto);

    @PutMapping("/api/cart")
    ResponseEntity<?> updateCart(@RequestBody CartDto cartDto);

    @DeleteMapping("/api/cart/{id}")
    ResponseEntity<?> deleteCart(@PathVariable("id") Integer cartId);
}
