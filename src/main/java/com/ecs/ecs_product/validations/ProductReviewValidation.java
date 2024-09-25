package com.ecs.ecs_product.validations;

import com.ecs.ecs_product.dto.ProductReviewDto;
import com.ecs.ecs_product.entity.ProductReview;

import java.util.Objects;

public class ProductReviewValidation {
    public static boolean validateProductReview(ProductReviewDto productReviewDto) {
        try {
            return Objects.nonNull(productReviewDto) &&
                    productReviewDto.getProductId() != 0 &&
                    productReviewDto.getCustomerId() != 0;
        }catch(Exception exception){
            return false;
        }

    }
}
