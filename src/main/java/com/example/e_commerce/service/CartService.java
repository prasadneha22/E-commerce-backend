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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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


    public CartRequest addCartItem(String token, CartItemRequest cartItemRequest) {
        Long userId = jwtService.extractUserId(token);
        String userRole = jwtService.extractUserRole(token);

        if(!"BUYER".equalsIgnoreCase(userRole)){
            throw new RuntimeException("Only buyers can add cart items.");
        }

        Users buyer = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found!"));

        Cart cart = cartRepository.findByUsers(buyer)
                .orElseThrow(()->new RuntimeException("Cart not found!"));

        Product product = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(()->new RuntimeException("Product not found!"));

        if(product.getStock() < cartItemRequest.getQuantity()){
            throw new RuntimeException("Not enough stock available for this product.");
        }

        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                        .filter(item ->item.getProduct().getId().equals(product.getId()))
                                .findFirst();

        if(existingCartItem.isPresent()){
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartItemRequest.getQuantity());
            cartItem.updateSubtotal();
        }else{
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.updateSubtotal();
            cart.getCartItems().add(cartItem);
        }
        product.setStock(product.getStock()- cartItemRequest.getQuantity());
        productRepository.save(product);

        cart.updateTotalAmount();
        Cart updatedCart = cartRepository.save(cart);
        return convertToCartResponse(updatedCart);

    }

    private CartRequest convertToCartResponse(Cart cart) {
        CartRequest response = new CartRequest();
        response.setCartId(cart.getId());
        response.setUserId(cart.getUsers().getId());
        response.setUserName(cart.getUsers().getFirstname() + " " + cart.getUsers().getLastname());
        response.setTotalAmount(cart.getTotalAmount());

        List<CartItemRequest> cartItemResponses = cart.getCartItems().stream().map(item -> {
            CartItemRequest itemResponse = new CartItemRequest();
            itemResponse.setId(item.getId());
            itemResponse.setProductId(item.getProduct().getId());
            itemResponse.setProductName(item.getProduct().getName());
            itemResponse.setProductPrice(item.getProduct().getPrice());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setSubtotal(item.getSubtotal());
            return itemResponse;
        }).collect(Collectors.toList());

        response.setCartItems(cartItemResponses);
        return response;
    }

    public List<CartItemRequest> getCartItems(String token) {
        Long userId = jwtService.extractUserId(token);
        String userRole = jwtService.extractUserRole(token);

        if(!"BUYER".equalsIgnoreCase(userRole)){
            throw new RuntimeException("Only buyers can access cart items.");
        }

       Users users = userRepository.findById(userId)
               .orElseThrow(()-> new RuntimeException("User not found!"));


       Cart cart = cartRepository.findByUsers(users)
               .orElseThrow(()->new RuntimeException("User not found!"));

       return cart.getCartItems().stream().map(item -> {
           CartItemRequest itemResponse = new CartItemRequest();
           itemResponse.setProductName(item.getProduct().getName());
           itemResponse.setProductPrice(item.getProduct().getPrice());
           itemResponse.setQuantity(item.getQuantity());
           itemResponse.setSubtotal(item.getSubtotal());
           return itemResponse;
       }).collect(Collectors.toList());

    }
}

