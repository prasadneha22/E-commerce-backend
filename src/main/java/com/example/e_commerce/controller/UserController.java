package com.example.e_commerce.controller;

import com.example.e_commerce.DTO.LoginDto;
import com.example.e_commerce.entity.Users;
import com.example.e_commerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody Users users){
        try{
            Users registerUser = userService.register(users);
            return ResponseEntity.status(HttpStatus.CREATED).body(registerUser);
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody LoginDto loginDto){
        Map<String,Object> response = userService.login(loginDto);
        if(response.containsKey("Error")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        }
        return ResponseEntity.ok(response);
    }




}
