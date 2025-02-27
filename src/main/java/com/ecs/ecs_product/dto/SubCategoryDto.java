package com.ecs.ecs_product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryDto {
    private Integer subCategoryId;
    private Integer categoryId;
    private String subCategoryName;
    private String subCategoryDescription;
    private String subCategoryImage;
}
