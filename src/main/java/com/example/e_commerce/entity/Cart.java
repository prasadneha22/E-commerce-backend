package com.example.e_commerce.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.User;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false,unique = true)
    private Users users;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private Set<CartItem> cartItems;

    private Double totalAmount = 0.0;

    public void updateTotalAmount(){
        this.totalAmount = cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();
    }
}
