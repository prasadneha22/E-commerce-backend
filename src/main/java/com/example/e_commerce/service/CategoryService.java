package com.example.e_commerce.service;

import com.example.e_commerce.DTO.CategoryDto;
import com.example.e_commerce.DTO.ProductDto;
import com.example.e_commerce.entity.Category;
import com.example.e_commerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    JwtService jwtService;

    public CategoryDto addCategory(String token, CategoryDto categoryDto) {

        String userRole = jwtService.extractUserRole(token);

        if(!"ADMIN".equalsIgnoreCase(userRole)){
            throw new RuntimeException("Unauthorized : only admins can add categories.");
        }
        Optional<Category> existCategory = categoryRepository.findByName(categoryDto.getName());
        if(existCategory.isPresent()){
            throw new RuntimeException("Category already exist!");
        }

        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        Category savedCategory = categoryRepository.save(category);

        return new CategoryDto(savedCategory.getId(),savedCategory.getName(), savedCategory.getDescription());

        
    }

    public List<CategoryDto> getAllCategories(String token) {

        String userRole = jwtService.extractUserRole(token);

        if(!"ADMIN".equalsIgnoreCase(userRole)){
            throw new RuntimeException("Unauthorized : Only admin can view categories.");

        }

        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> new CategoryDto(category.getId(),category.getName(), category.getDescription()))
                .collect(Collectors.toList());
    }
}
