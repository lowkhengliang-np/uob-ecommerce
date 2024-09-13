package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.repo.ProductRepo;
import java.util.List;
import com.example.demo.models.Product;

@Controller
public class ProductController {
    private final ProductRepo productRepo;
    
    @Autowired
    // Dependency injection = when Springboots creates an instances of Product controllers
    // it will automatically create an instance of ProductRepo and pass it into the new instance
    // of product controller
    public ProductController(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping("/products")
    public String listProducts(Model model){
        List<Product> products = productRepo.findAll();
        model.addAttribute("products", products);
        return "products/index";
    }
}
