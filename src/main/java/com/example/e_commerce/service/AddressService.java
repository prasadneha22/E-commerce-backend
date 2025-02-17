package com.example.e_commerce.service;

import com.example.e_commerce.entity.Address;
import com.example.e_commerce.entity.Users;
import com.example.e_commerce.repository.AddressRepository;
import com.example.e_commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private JwtService jwtService;

    public Address addAddress(String token, Address address) {

        Long userId = jwtService.extractUserId(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("user not found"));

        address.setUser(user);
        return addressRepository.save(address);
    }
}
