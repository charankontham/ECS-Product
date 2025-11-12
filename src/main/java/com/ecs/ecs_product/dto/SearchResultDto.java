package com.ecs.ecs_product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDto {
    private Integer itemId;
    private String itemType;
    private String itemName;
    private String itemCategory;
    private Double relevanceScore;
}
