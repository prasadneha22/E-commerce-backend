package com.example.e_commerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/profile")
public class ProfileController {


//    @GetMapping("/me")
//    public ResponseEntity<Map<String, Object>> getUserDetails(@RequestHeader("Authorization") String token){
//        if(token.startsWith("Bearer ")){
//            token = token.substring(7);
//        }
//        try{
//
//        }
//    }
}
