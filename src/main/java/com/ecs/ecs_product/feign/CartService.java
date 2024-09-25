package com.ecs.ecs_product.feign;

import com.ecs.ecs_product.dto.CartDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("CART-SERVICE")
public interface CartService {
    @GetMapping("/{id}")
    ResponseEntity<?> getCart(@PathVariable("id") int cartId);

    @GetMapping("/getCartByCustomerId/{id}")
    ResponseEntity<?> getCartByCustomerId(@PathVariable("id") int customerId);

    @GetMapping("/getCartsByProductId/{id}")
    ResponseEntity<List<CartDto>> getCartsByProductId(@PathVariable("id") int productId);

    @PostMapping
    ResponseEntity<?> addCart(@RequestBody CartDto cartDto);

    @PutMapping
    ResponseEntity<?> updateCart(@RequestBody CartDto cartDto);

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteCart(@PathVariable("id") int cartId);
}
