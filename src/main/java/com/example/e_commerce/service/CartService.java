package com.example.e_commerce.service;

import com.example.e_commerce.DTO.CartItemRequest;
import com.example.e_commerce.DTO.CartRequest;
import com.example.e_commerce.entity.Cart;
import com.example.e_commerce.entity.CartItem;
import com.example.e_commerce.entity.Product;
import com.example.e_commerce.entity.Users;
import com.example.e_commerce.repository.CartRepository;
import com.example.e_commerce.repository.ProductRepository;
import com.example.e_commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@Service
public class CartService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public Cart addCart(String token, CartRequest cartRequest) {

        Long userId = jwtService.extractUserId(token);
        String userRole = jwtService.extractUserRole(token);

        if(!"BUYER".equalsIgnoreCase(userRole)){
            throw new RuntimeException("Only buyer can add cart.");
        }

        Users buyer = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!!"));

        if(cartRepository.findByUsers(buyer).isPresent()){
            throw new RuntimeException("Buyer already has a cart");
        }

        Cart cart1 = new Cart();
        cart1.setUsers(buyer);
        cart1.setCartItems(new HashSet<>());
        for (CartItemRequest itemRequest : cartRequest.getCartItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            CartItem cartItem = new CartItem();
            cartItem.setCart(cart1); // ✅ Prevents null error
            cartItem.setProduct(product);
            cartItem.setQuantity(itemRequest.getQuantity());
            cartItem.updateSubtotal();

            cart1.getCartItems().add(cartItem);
        }

        // Update total amount
        cart1.updateTotalAmount();

        // Save cart
        return cartRepository.save(cart1);
    }


    }

