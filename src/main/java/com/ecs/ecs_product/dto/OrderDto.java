package com.ecs.ecs_product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Integer orderId;
    private Integer customerId;
    private Integer addressId;
    private Integer paymentType;
    private Integer paymentStatus;
    private Float shippingFee;
    private LocalDateTime orderDate;
    private Integer orderStatus;
}
