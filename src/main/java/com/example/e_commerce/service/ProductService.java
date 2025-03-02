package com.example.e_commerce.service;

import com.example.e_commerce.DTO.ProductDto;
import com.example.e_commerce.entity.Category;
import com.example.e_commerce.entity.Product;
import com.example.e_commerce.entity.Users;
import com.example.e_commerce.repository.CategoryRepository;
import com.example.e_commerce.repository.ProductRepository;
import com.example.e_commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;


    public ProductDto addProduct(String token, ProductDto productDto) {

        String userRole = jwtService.extractUserRole(token);
        Long userId = jwtService.extractUserId(token);

        System.out.println("User id is : " + userId);
        System.out.println("role is : " + userRole);


        if(!"SELLER".equalsIgnoreCase(userRole)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access Denied! Only Sellers can add products");
        }

       Users seller = userRepository.findById(userId)
               .orElseThrow(()->new RuntimeException("user not found!"));

        Category category = categoryRepository.findById(productDto.getCategoryId())
                        .orElseThrow(()->new RuntimeException("Category not found!"));

        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setStock(productDto.getStock());
        product.setAttribute(productDto.getAttribute());
        product.setPrice(productDto.getPrice());
        product.setUser(seller);
        product.setCategory(category);


        Product savedProduct = productRepository.save(product);


        ProductDto productDto1 = new ProductDto();
        productDto1.setName(savedProduct.getName());
        productDto1.setDescription(savedProduct.getDescription());
        productDto1.setAttribute(savedProduct.getAttribute());
        productDto1.setStock(savedProduct.getStock());
        productDto1.setPrice(savedProduct.getPrice());
        productDto1.setIsActive(savedProduct.getIsActive());
        productDto1.setCategoryId(savedProduct.getCategory() !=null ? savedProduct.getCategory().getId() : null);

        return productDto1;

    }


    public ProductDto updateProduct(String token, Long productId, ProductDto productDto) {

        Long userId = jwtService.extractUserId(token);
        String userRole = jwtService.extractUserRole(token);

        if(!"SELLER".equalsIgnoreCase(userRole)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied! Only sellers can update products.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found!"));

        if(!product.getUser().getId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized! you can only update your own product.");
        }

        product.setDescription(productDto.getDescription());
        product.setStock(productDto.getStock());
        product.setAttribute(productDto.getAttribute());
        product.setPrice(productDto.getPrice());
        product.setIsActive(productDto.getIsActive());

        Product updatedProduct = productRepository.save(product);
        return new ProductDto(updatedProduct);
    }
}
