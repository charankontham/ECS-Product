package com.ecs.ecs_product.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="sub_category")
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subCategoryId;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name ="sub_category_name")
    private String subCategoryName;

    @Column(name = "sub_category_description")
    private String subCategoryDescription;

    @Column(name ="sub_category_image")
    private String subCategoryImage;

    @Override
    public String toString() {
        return "\nsubCategoryId=" + subCategoryId;
    }
}

