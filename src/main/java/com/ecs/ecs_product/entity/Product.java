package com.ecs.ecs_product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @Column(name = "product_category_id")
    private Integer productCategoryId;

    @Column(name = "sub_category_id")
    private Integer subCategoryId;

    @Column(name ="product_brand_id")
    private Integer productBrandId;

    @Column(name = "product_name")
    private String productName;

    @Column(name ="product_description")
    private String productDescription;

    @Column(name = "product_price")
    private Float productPrice;

    @Column(name ="product_quantity")
    private Integer productQuantity;

    @Column(name ="product_image")
    private String productImage;

    @Column(name = "product_color")
    private String productColor;

    @Column(name = "product_weight")
    private Float productWeight;

    @Column(name = "date_added", updatable = false)
    private LocalDateTime dateAdded;

    @Column(name = "date_modified")
    private LocalDateTime dateModified;

    @Column(name = "product_dimensions")
    private String productDimensions;

    @Column(name = "product_condition")
    private String productCondition;

    @Override
    public String toString() {
        return
                "\nproductId=" + productId;
    }
}
