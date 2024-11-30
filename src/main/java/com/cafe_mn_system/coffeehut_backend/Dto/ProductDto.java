package com.cafe_mn_system.coffeehut_backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDto {

    Integer id;

    String name;

    String description;

    Double price;

    String status;

    Integer categoryId;

    String categoryName;

    public ProductDto(Integer id, String name, String description, Double price, String status, Integer categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
