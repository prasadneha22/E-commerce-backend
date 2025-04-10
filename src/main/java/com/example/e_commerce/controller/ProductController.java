package com.example.e_commerce.controller;

import com.example.e_commerce.DTO.ProductDto;
import com.example.e_commerce.entity.Product;
import com.example.e_commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/get")
    public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "name") String sortBy){
        try{
            Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy));
            Page<ProductDto> productPage = productService.getAllProducts(pageable);
            return ResponseEntity.ok(productPage);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id){
        try{
            ProductDto product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@RequestHeader("Authorization") String token, @PathVariable Long productId){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        try{
            productService.deleteProduct(token,productId);
            return ResponseEntity.ok("Product deleted successfully");
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected Error occurred.");
        }
    }



}
