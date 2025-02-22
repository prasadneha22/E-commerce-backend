package com.example.e_commerce.service;

import com.example.e_commerce.DTO.UserDto;
import com.example.e_commerce.entity.Users;
import com.example.e_commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProfileService {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    public UserDto getUserDetails(String token) {

        try{
            Long userId = jwtService.extractUserId(token);
            return userRepository.findById(userId)
                    .map(users -> new UserDto(
                            users.getId(),
                            users.getFirstname(),
                            users.getLastname(),
                            users.getEmail(),
                            users.getPhoneNumber(),
                            users.getAddresses()))
                    .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found"));

        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid Jwt token", e);
        }

    }

    public UserDto updateProfile(String token, UserDto userDto) {

        try{
            Long userId = jwtService.extractUserId(token);
            System.out.println(userId);

            Users users = userRepository.findById(userId)
                    .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found!!"));

            users.setFirstname(userDto.getFirstname());
            users.setLastname(userDto.getLastname());
            users.setPhoneNumber(userDto.getPhoneNumber());

            userRepository.save(users);
            System.out.println("user updated successfully");

            return new UserDto(
                    users.getId(),
                    users.getFirstname(),
                    users.getLastname(),
                    users.getEmail(),
                    users.getPhoneNumber(),
                    users.getAddresses()
            );
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error updating profile",e);
        }
    }
}
