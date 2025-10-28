package com.ecs.ecs_product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemEnriched {
    private Integer orderItemId;
    private Integer orderId;
    private ProductFinalDto product;
    private Integer orderItemStatus;
    private Integer invoiceId;
}
