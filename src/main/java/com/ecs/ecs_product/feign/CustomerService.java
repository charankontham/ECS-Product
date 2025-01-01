package com.ecs.ecs_product.feign;

import com.ecs.ecs_product.config.FeignClientConfig;
import com.ecs.ecs_product.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ECS-CUSTOMER", configuration = FeignClientConfig.class)
public interface CustomerService {
    @GetMapping("/api/customer/getByEmail/{email}")
    ResponseEntity<CustomerDto> getCustomerByEmail(@PathVariable("email") String email);
}