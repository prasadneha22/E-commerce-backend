package com.example.e_commerce.controller;

import com.example.e_commerce.entity.Address;
import com.example.e_commerce.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/add")
    public ResponseEntity<?> addAddress(@RequestHeader("Authorization") String token, @RequestBody Address address){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        try{
            Address savedAddress = addressService.addAddress(token,address);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAddress);

        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred!");
        }

    }





}
