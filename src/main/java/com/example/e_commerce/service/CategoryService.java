package com.example.e_commerce.service;

import com.example.e_commerce.DTO.CategoryDto;
import com.example.e_commerce.DTO.ProductDto;
import com.example.e_commerce.entity.Category;
import com.example.e_commerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public List<CategoryDto> getAllCategories() {


        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> new CategoryDto(category.getId(),category.getName(), category.getDescription()))
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found with id : " + categoryId));

        return new CategoryDto(category);
    }

    public CategoryDto updateCategory(String token, Long categoryId, CategoryDto categoryDto) {

        String userRole = jwtService.extractUserRole(token);
        if(!"ADMIN".equalsIgnoreCase(userRole)){
            throw new RuntimeException("Unauthorized : Only admin can update the categories");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + categoryId));

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        return new CategoryDto(updatedCategory);
    }

    public void deleteCategory(String token, Long id) {

        String userRole = jwtService.extractUserRole(token);

        if(!"ADMIN".equalsIgnoreCase(userRole)){
            throw new RuntimeException("Unauthorized : Only admin can delete the categories");
        }
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id :" +  id));

        categoryRepository.delete(category);
    }
}
