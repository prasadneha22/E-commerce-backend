package com.example.e_commerce.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.juli.logging.Log;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   private String firstname;
   private String lastname;
   private String email;
   private String password;
   private String phoneNumber;
   private boolean isActive = true;
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private Role role = Role.BUYER;

   @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
   @JsonManagedReference
   private List<Address> addresses;

}