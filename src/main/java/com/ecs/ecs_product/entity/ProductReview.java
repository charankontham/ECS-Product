package com.ecs.ecs_product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
@Getter
@Table(name = "product_review")
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "product_review")
    private String productReview;

    @Column(name = "product_rating")
    private float productRating;
}
