package com.ecs.ecs_product.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrendingSearch {
    private String searchQuery;
    private Double frequency;
    private int uniqueUserCount;
    private LocalDateTime lastSearchedAt;
    private Integer hoursSinceLastSearch;
    private Double recencyBoost;
    private Double finalScore;
}
