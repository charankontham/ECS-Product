package com.ecs.ecs_product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithRelevance {
    private Integer productId;
    private Integer productCategoryId;
    private Integer subCategoryId;
    private Integer productBrandId;
    private String productName;
    private String productDescription;
    private Float productPrice;
    private Integer productQuantity;
    private String productImage;
    private String productColor;
    private Float productWeight;
    private LocalDateTime dateAdded;
    private LocalDateTime dateModified;
    private String productDimensions;
    private String productCondition;
    private BigDecimal relevance;
}
