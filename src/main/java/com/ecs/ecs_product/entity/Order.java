package com.ecs.ecs_product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_details")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @Column(name = "customer_id")
    private int customerId;

    @Column(name ="address_id")
    private int addressId;

    @Column(name = "product_ids")
    private String productIds;

    @Column(name = "product_quantities")
    private String productQuantities;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "ordered_date")
    private Date orderDate;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(name = "delivery_status")
    private String shippingStatus;
}
