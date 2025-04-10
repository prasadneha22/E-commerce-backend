package com.example.e_commerce.controller;

import com.example.e_commerce.DTO.CartItemRequest;
import com.example.e_commerce.DTO.CartRequest;
import com.example.e_commerce.DTO.CategoryDto;
import com.example.e_commerce.entity.Cart;
import com.example.e_commerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

    @PostMapping("/add-cart-item")
    public ResponseEntity<?> addCartItem(@RequestHeader("Authorization") String token, @RequestBody CartItemRequest cartItemRequest){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        try{
            CartRequest updatedCart = cartService.addCartItem(token,cartItemRequest);
            return ResponseEntity.ok(updatedCart);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error : "+ e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/cart-items")
    public ResponseEntity<List<CartItemRequest>> getCartItems(@RequestHeader("Authorization") String token){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        try{
            List<CartItemRequest> cartItems = cartService.getCartItems(token);
            return ResponseEntity.ok(cartItems);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/cart-update")
    public ResponseEntity<CartRequest> updateCartItem(@RequestHeader("Authorization") String token, @RequestBody CartItemRequest cartItemRequest) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            CartRequest updatedCart = cartService.updateCartItemQuantity(token, cartItemRequest);
            return ResponseEntity.ok(updatedCart);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
