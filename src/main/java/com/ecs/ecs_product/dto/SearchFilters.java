package com.ecs.ecs_product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchFilters {
    private String keyword;
    private List<Integer> categories;
    private List<Integer> subCategories;
    private List<Integer> brands;
    private List<String> condition;
    private List<String> colors;
    private Float minRating;
    private Integer minDiscount;
    private List<Integer> priceRange;
    private String sortBy;
}
