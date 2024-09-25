package com.ecs.ecs_product.feign;

import com.ecs.ecs_product.dto.OrderDto;
import com.ecs.ecs_product.dto.OrderFinalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("ORDER-SERVICE")
public interface OrderService {
    @GetMapping("/{id}")
    ResponseEntity<OrderFinalDto> getOrderById(@PathVariable("id") int orderId);

    @GetMapping("/")
    ResponseEntity<List<OrderFinalDto>> getAllOrders();

    @GetMapping("/getOrdersByCustomerId/{id}")
    ResponseEntity<List<OrderFinalDto>> getAllOrdersByCustomerId(@PathVariable("id") int customerId);

    @GetMapping("/getOrdersByProductId/{id}")
    ResponseEntity<List<OrderDto>> getAllOrdersByProductId(@PathVariable("id") int productId);

    @PostMapping
    ResponseEntity<?> addOrder(@RequestBody OrderDto orderDto);

    @PutMapping
    ResponseEntity<?> updateOrder(@RequestBody OrderDto orderDto);

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteOrder(@PathVariable("id") int orderId);
}
