package com.example.e_commerce.controller;

import com.example.e_commerce.DTO.UserDto;
import com.example.e_commerce.entity.Users;
import com.example.e_commerce.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String token){
       try{
           if(token ==null || !token.startsWith("Bearer ")){
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Jwt token");
           }
           token = token.substring(7);

           UserDto userDto = profileService.getUserDetails(token);
           return ResponseEntity.ok(userDto);
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user details: " + e.getMessage());
       }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String token, @RequestBody UserDto userDto){
        try {
            if(token ==null || !token.startsWith("Bearer ")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing Jwt token.");
            }
            token = token.substring(7);

            UserDto updatedUser = profileService.updateProfile(token,userDto);
            return ResponseEntity.ok(updatedUser);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Updating profile: " +e.getMessage());
        }
    }

}
