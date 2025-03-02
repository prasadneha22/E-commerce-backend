package com.example.e_commerce.controller;

import com.example.e_commerce.DTO.ProductDto;
import com.example.e_commerce.entity.Product;
import com.example.e_commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestHeader("Authorization") String token, @RequestBody ProductDto productDto){
       if(token.startsWith("Bearer ")){
           token = token.substring(7);
       }
       try{
           ProductDto productResponse = productService.addProduct(token,productDto);
           return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
       }catch (RuntimeException e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
       }
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<?> updateProduct(@RequestHeader("Authorization") String token, @PathVariable Long productId ,@RequestBody ProductDto productDto){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        try{
            ProductDto updateResponse = productService.updateProduct(token,productId,productDto);

            return ResponseEntity.ok(updateResponse);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(("Error " + e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

}
