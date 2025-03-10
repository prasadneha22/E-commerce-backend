package com.example.e_commerce.controller;


import com.example.e_commerce.DTO.CategoryDto;
import com.example.e_commerce.DTO.ProductDto;
import com.example.e_commerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.PrimitiveIterator;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add-category")
    public ResponseEntity<?> addCategory(@RequestHeader("Authorization") String token, @RequestBody CategoryDto categoryDto){

        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        try{
            CategoryDto categoryResponse = categoryService.addCategory(token,categoryDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/category-list")
    public ResponseEntity<?> getAllCategories(){

        try{
            List<CategoryDto> categoryList = categoryService.getAllCategories();
            return ResponseEntity.ok(categoryList);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error :" + e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long categoryId){
        try{
            CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
            return ResponseEntity.ok(categoryDto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        }
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<?> updateCategory(@RequestHeader("Authorization") String token ,@PathVariable Long categoryId, @RequestBody CategoryDto categoryDto){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        try{
            CategoryDto updateResponse = categoryService.updateCategory(token,categoryId,categoryDto);
            return ResponseEntity.ok(updateResponse);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@RequestHeader("Authorization") String token,@PathVariable Long id){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        try{
            categoryService.deleteCategory(token,id);
            return ResponseEntity.ok("Category deleted successfully.");

        }catch (ResponseStatusException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getReason());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("something went wrong!");
        }
    }


}

