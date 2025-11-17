package com.ecs.ecs_product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ProductWithRelevanceProjection {
    Integer getProductId();
    Integer getProductCategoryId();
    Integer getSubCategoryId();
    Integer getProductBrandId();
    String getProductName();
    String getProductDescription();
    Float getProductPrice();
    Integer getProductQuantity();
    String getProductImage();
    String getProductColor();
    Float getProductWeight();
    LocalDateTime getDateAdded();
    LocalDateTime getDateModified();
    String getProductDimensions();
    String getProductCondition();
    BigDecimal getRelevance();
}
