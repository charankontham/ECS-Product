package com.ecs.ecs_product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductReviewDto {
    private Integer reviewId;
    private Integer productId;
    private Integer customerId;
    private String customerName;
    private Integer productRating;
    private String reviewHeadline;
    private String productReview;
    private LocalDateTime reviewDate;
}
