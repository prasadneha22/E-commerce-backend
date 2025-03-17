package com.example.e_commerce.DTO;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CartRequest {
    private Long cartId;
    private Long userId;
    private String userName;
    private List<CartItemRequest> cartItems;
    private Double totalAmount;
}
