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
public class OrderFinalDto {
    private Integer orderId;
    private CustomerDto customer;
    private AddressDto shippingAddress;
    private List<OrderItemEnriched> orderItems;
    private Float itemsSubTotal;
    private Float shippingFee;
    private Float totalTax;
    private Float totalOrderValue;
    private LocalDateTime orderDate;
    private Integer orderStatus;
    private Integer paymentType;
    private Integer paymentStatus;
    private Integer invoiceId;
}
