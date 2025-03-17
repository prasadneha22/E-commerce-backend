package com.example.e_commerce.DTO;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long id;
    private Long productId;
    private String productName;
    private Double productPrice;
    private Integer quantity;
    private Double subtotal;
}
