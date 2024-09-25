package com.ecs.ecs_product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;

    @Column(name ="customer_id")
    private int customerId;

    @Column(name = "product_ids")
    private String productIds;

    @Column(name = "quantities")
    private String quantities;

}
