package com.example.e_commerce.DTO;

import com.example.e_commerce.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String name;
    private String description;
    private Integer stock;
    private String attribute;
    private Double price;
    private Boolean isActive = true;
    private Long categoryId;


    public ProductDto(Long id, String name, String description, Integer stock, String attribute, Double price, Boolean isActive, Long aLong) {
    }

    public ProductDto(Product product) {
        this.name = product.getName();
        this.description = product.getDescription();
        this.stock = product.getStock();
        this.attribute = product.getAttribute();
        this.price = product.getPrice();
        this.isActive = product.getIsActive();
        this.categoryId = (product.getCategory() != null) ? product.getCategory().getId() : null;
    }

    public ProductDto(Long id, String name, String description) {
    }
}
