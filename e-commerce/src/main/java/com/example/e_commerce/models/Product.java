package com.example.e_commerce.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Product extends BaseEntity {

    private String name;

    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private Integer stock;

}