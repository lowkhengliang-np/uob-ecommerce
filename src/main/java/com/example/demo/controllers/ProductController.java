package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.ProductRepo;

import jakarta.validation.Valid;

import java.util.List;
import com.example.demo.models.Product;

@Controller
public class ProductController {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    @Autowired
    // Dependency injection = when Springboots creates an instances of Product controllers
    // it will automatically create an instance of ProductRepo and pass it into the new instance
    // of product controller
    public ProductController(ProductRepo productRepo, CategoryRepo categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    @GetMapping("/products")
    public String listProducts(Model model){
        List<Product> products = productRepo.findAllWithCategories();
        model.addAttribute("products", products);
        return "products/index";
    }

    // When we want to add forms, we always need 2 routes,
    // 1 to display 1 to process
    @GetMapping("/products/create")
    public String showCreateProductForm(Model model){
        // Send an empty instance of the product model to the template

        var newProduct = new Product();
        // add the instance of the new product model to the view model

        // find all the categories and add it to the view model
        model.addAttribute("categories", categoryRepo.findAll());

        model.addAttribute("product", newProduct);
        return "products/create";
    }
    // The results of the validation will be in the bindingResult parameter
    @PostMapping("/products/create")
    public String processCreateProductForm(@Valid @ModelAttribute Product newProduct, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("categories", categoryRepo.findAll());
            return "products/create";
        }
        
        // save the new product
        productRepo.save(newProduct);
        // a redirect tells the client to go to a different URL
        return "redirect:/products";
    }
    
    @GetMapping("/products/{id}") //URL parameter
    public String productDetails(@PathVariable Long id, Model model){
        // find the product with the matching Id
        var product = productRepo.findById(id).orElseThrow(()-> new RuntimeException());
        model.addAttribute("product", product);

        return "products/details";
    }

    @GetMapping("/products/{id}/edit")
    public String showUpdateProduct(@PathVariable Long id, Model model){
        var product = productRepo.findById(id).orElseThrow(()-> new RuntimeException());
        model.addAttribute("product", product);

        //find all categories
        model.addAttribute("categories", categoryRepo.findAll());

        return "products/edit";
    }

    @PostMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute Product product, BindingResult bindingResult, Model model){
        product.setId(id); //Ensure we are updating the correct Id
        
        if (bindingResult.hasErrors()){
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryRepo.findAll());
            return "redirect:/prodcuts" + id + "/edit";
        }
        
        productRepo.save(product);
        return "redirect:/products";
    }

    // We have two routes for deleting
    // 1 showing a delete form (asking the user if they really want to delete)
    // 2 process the delete

    @GetMapping("/products/{id}/delete")
    public String showDeleteProductForm(@PathVariable Long id, Model model){
        var product = productRepo.findById(id).orElseThrow(()-> new RuntimeException());
        model.addAttribute("product", product);

        return "products/delete";
    } 

    //Process delete
    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id){
        productRepo.deleteById(id);
        return "redirect:/products";
    }
}
