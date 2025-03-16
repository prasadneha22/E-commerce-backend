package com.example.e_commerce.controller;

import com.example.e_commerce.DTO.CartRequest;
import com.example.e_commerce.DTO.CategoryDto;
import com.example.e_commerce.entity.Cart;
import com.example.e_commerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add-cart")
    public ResponseEntity<?> addCart(@RequestHeader("Authorization") String token, @RequestBody CartRequest cartRequest){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        try{
            Cart createdCart = cartService.addCart(token,cartRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(cartRequest);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }

    }
}
