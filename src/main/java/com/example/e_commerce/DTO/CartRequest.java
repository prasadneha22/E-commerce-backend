package com.example.e_commerce.DTO;

import lombok.Data;

import java.util.Set;

@Data
public class CartRequest {
    private Set<CartItemRequest> cartItems;
}
