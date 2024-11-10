package com.ecs.ecs_product.feign;

import com.ecs.ecs_product.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ECS-CUSTOMER", configuration = FeignClientConfig.class)
public interface CustomerService {
    @GetMapping("/api/public/authentication/getByUsername/{username}")
    ResponseEntity<UserDto> getUserByUsername(@PathVariable("username") String username);
}
