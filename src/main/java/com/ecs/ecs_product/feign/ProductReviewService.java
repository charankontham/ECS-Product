package com.ecs.ecs_product.feign;

import com.ecs.ecs_product.dto.ProductReviewDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "ECS-REVIEWS", configuration = FeignClientConfig.class)
public interface ProductReviewService {
    @GetMapping("/api/productReview/getReviewsByProductId/{id}")
    ResponseEntity<List<ProductReviewDto>> getProductReviewsByProductId(@PathVariable("id") Integer productId);
}
