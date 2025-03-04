//package com.example.e_commerce.controller;
//
//
//import com.example.e_commerce.DTO.CategoryDto;
//import com.example.e_commerce.DTO.ProductDto;
//import com.example.e_commerce.service.CategoryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.PrimitiveIterator;
//
//@RestController
//@RequestMapping("/category")
//public class CategoryController {
//
//    @Autowired
//    private CategoryService categoryService;
//
//    @PostMapping("/add-category")
//    public ResponseEntity<?> addCategory(@RequestHeader("Authorization") String token, @RequestBody CategoryDto categoryDto){
//
//        if(token.startsWith("Bearer ")){
//            token = token.substring(7);
//        }
//        try{
//            ProductDto productResponse = categoryService.addCategory(token,categoryDto);
//            return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
//        }catch (RuntimeException e){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
//        }catch (Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
//        }
//    }
//
//    }
//
