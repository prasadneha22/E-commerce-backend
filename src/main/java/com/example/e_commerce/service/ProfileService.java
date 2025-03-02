package com.example.e_commerce.service;

import com.example.e_commerce.DTO.UserDto;
import com.example.e_commerce.entity.Users;
import com.example.e_commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<UserDto> getAllProfiles(String token) {

        try{
            String userRole = jwtService.extractUserRole(token);
            Long userId = jwtService.extractUserId(token);

            System.out.println("User id is : " + userId);
            System.out.println("role is : " + userRole);

            Users adminUser = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin user not found"));

            if(!"ADMIN".equalsIgnoreCase(userRole)){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access Denied! Only Admins can view all users");
            }

            return userRepository.findAll().stream()
                    .map( u -> new UserDto(
                            u.getId(),
                            u.getFirstname(),
                            u.getLastname(),
                            u.getEmail(),
                            u.getPhoneNumber(),
                            u.getCreatedAt(),
                            u.getUpdatedAt(),
                            u.getRole(),
                            u.getAddresses()))
                    .collect(Collectors.toList());
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error fetching users",e);
        }




    }
}
