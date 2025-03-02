package com.example.e_commerce.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Integer stock;
    private String attribute;
    private Double price;
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="seller_id",nullable = false)
    private Users user;


    public Product(String name, String description, Integer stock, String attribute, Double price, Boolean active, Users seller, Category category) {
    }
}
