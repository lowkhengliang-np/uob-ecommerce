package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.TagRepo;

import jakarta.validation.Valid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.demo.models.Product;
import com.example.demo.models.Tag;

@Controller
public class ProductController {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final TagRepo tagRepo;
    @Autowired
    // Dependency injection = when Springboots creates an instances of Product controllers
    // it will automatically create an instance of ProductRepo and pass it into the new instance
    // of product controller
    public ProductController(ProductRepo productRepo, CategoryRepo categoryRepo, TagRepo tagRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.tagRepo = tagRepo;
    }

    @GetMapping("/products")
    public String listProducts(Model model){
        List<Product> products = productRepo.findAllWithCategoriesAndTags();
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
        model.addAttribute("allTags", tagRepo.findAll());

        return "products/create";
    }
    // The results of the validation will be in the bindingResult parameter
    @PostMapping("/products/create")
    public String processCreateProductForm(@Valid @ModelAttribute Product newProduct, BindingResult bindingResult, Model model,
    @RequestParam(required=false) List<Long> tagIds){
        if (bindingResult.hasErrors()){
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("allTags", tagRepo.findAll());
            return "products/create";
        }
        //check if the user has selected any tags
        if (tagIds != null){
            var tags = new HashSet<>(tagRepo.findAllById(tagIds));
            newProduct.setTags(tags);
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
        model.addAttribute("allTags", tagRepo.findAll());

        return "products/edit";
    }

    @PostMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute Product product, BindingResult bindingResult, Model model,
    @RequestParam(required=false) List<Long> tagIds){
        product.setId(id); //Ensure we are updating the correct Id
        
        if (bindingResult.hasErrors()){
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("allTags", tagRepo.findAll());
            return "redirect:/prodcuts" + id + "/edit";
        }

        //update the tags on the products
        if (tagIds != null && !tagIds.isEmpty()){
            Set<Tag> tags = new HashSet<Tag>(tagRepo.findAllById(tagIds));
            product.setTags(tags);
        }
        else{
            product.getTags().clear();
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
