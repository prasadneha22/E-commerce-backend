package com.example.e_commerce.service;

import com.example.e_commerce.entity.Users;
import com.example.e_commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

//    @Autowired
//    AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users users) {

        Users existingUser = userRepository.findByEmail(users.getEmail());
        if(existingUser!=null){
            throw  new RuntimeException("Email Already Existed!");
        }

        Users users1 = new Users();
        users1.setFullName(users.getFullName());
        users1.setEmail(users.getEmail());
        users1.setPassword(encoder.encode(users.getPassword()));
        users1.setRole(users.getRole());

        return userRepository.save(users1);
    }
}
