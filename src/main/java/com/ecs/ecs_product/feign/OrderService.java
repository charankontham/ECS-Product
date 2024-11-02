package com.ecs.ecs_product.feign;

import com.ecs.ecs_product.dto.CartDto;
import com.ecs.ecs_product.dto.OrderDto;
import com.ecs.ecs_product.dto.OrderFinalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient("ECS-ORDER")
public interface OrderService {
    @GetMapping("/api/order/{id}")
    ResponseEntity<OrderFinalDto> getOrderById(@PathVariable("id") int orderId);

    @GetMapping("/api/order/")
    ResponseEntity<List<OrderFinalDto>> getAllOrders();

    @GetMapping("/api/order/getOrdersByCustomerId/{id}")
    ResponseEntity<List<OrderFinalDto>> getAllOrdersByCustomerId(@PathVariable("id") int customerId);

    @GetMapping("/api/order/getOrdersByProductId/{id}")
    ResponseEntity<List<OrderDto>> getAllOrdersByProductId(@PathVariable("id") int productId);

    @PostMapping("/api/order")
    ResponseEntity<?> addOrder(@RequestBody OrderDto orderDto);

    @PutMapping("/api/order")
    ResponseEntity<?> updateOrder(@RequestBody OrderDto orderDto);

    @DeleteMapping("/api/order/{id}")
    ResponseEntity<String> deleteOrder(@PathVariable("id") int orderId);

    @GetMapping("/api/cart/{id}")
    ResponseEntity<?> getCart(@PathVariable("id") int cartId);

    @GetMapping("/api/cart/getCartByCustomerId/{id}")
    ResponseEntity<?> getCartByCustomerId(@PathVariable("id") int customerId);

    @GetMapping("api/cart/getCartsByProductId/{id}")
    ResponseEntity<List<CartDto>> getCartsByProductId(@PathVariable("id") int productId);

    @PostMapping("/api/cart")
    ResponseEntity<?> addCart(@RequestBody CartDto cartDto);

    @PutMapping("/api/cart")
    ResponseEntity<?> updateCart(@RequestBody CartDto cartDto);

    @DeleteMapping("/api/cart/{id}")
    ResponseEntity<?> deleteCart(@PathVariable("id") int cartId);
}
