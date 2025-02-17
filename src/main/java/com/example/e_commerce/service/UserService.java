package com.example.e_commerce.service;

import com.example.e_commerce.DTO.LoginDto;
import com.example.e_commerce.entity.Address;
import com.example.e_commerce.entity.Role;
import com.example.e_commerce.entity.Users;
import com.example.e_commerce.repository.AddressRepository;
import com.example.e_commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users users) {

        Users existingUser = userRepository.findByEmail(users.getEmail());
        if(existingUser!=null){
            throw new RuntimeException("Email Already Existed!");
        }


        users.setPassword(encoder.encode(users.getPassword()));
        users.setCreatedAt(LocalDateTime.now());
        users.setUpdatedAt(LocalDateTime.now());


        return userRepository.save(users);
    }

    public Map<String, Object> login(LoginDto loginDto) {
        Map<String,Object> response = new HashMap<>();

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
            );

            if(authentication.isAuthenticated()){
                Users user = userRepository.findByEmail(loginDto.getEmail());
                if(user ==  null){
                    response.put("msg", "User not found!");
                    response.put("status", 404);
                    return response;
                }

                String token = jwtService.generateToken(user);

                Map<String,Object> userData = new HashMap<>();

                userData.put("id",user.getId());
                userData.put("email",user.getEmail());
                userData.put("firstName",user.getFirstname());
                userData.put("lastname",user.getLastname());
                userData.put("role",user.getRole());
                userData.put("phoneNumber",user.getPhoneNumber());
                userData.put("createdAt",user.getCreatedAt());
                userData.put("updatedAt",user.getUpdatedAt());

                response.put("msg","User Logged-in Successfully");
                response.put("data",userData);
                response.put("token",token);
                response.put("status",200);
                return response;
            }
        }catch (Exception e){
            response.put("error","Invalid credentials!" +  e.getMessage());
        }
        return response;
    }
}
