package com.example.demo.services;

import java.util.*;

import org.springframework.stereotype.Service;

import com.example.demo.models.CartItem;
import com.example.demo.models.Product;
import com.example.demo.models.User;
import com.example.demo.repo.CartItemRepo;

import jakarta.transaction.Transactional;

@Service
public class CartItemService {
    private final CartItemRepo cartItemRepo;
    public CartItemService(CartItemRepo cartItemRepo){
        this.cartItemRepo = cartItemRepo;
    }
    
    // When a method is transactional, if throws an exception, all DB writes and update will be undo    
    @Transactional
    public CartItem addToCart(User user, Product product, int quantity){
        //if product alr exists vs product not in cart

        Optional<CartItem> existingItem = cartItemRepo.findByUserAndProduct(user, product);
        if (existingItem.isPresent()){
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity()+1);
            return cartItemRepo.save(cartItem);
        } else {
            CartItem newItem = new CartItem(user, product, quantity);
            return cartItemRepo.save(newItem);
        }
    }

    public List<CartItem> findByUser(User user){
        // for future business logic
        // example recommendations, discount code, out of stock notice
        return cartItemRepo.findByUser(user);
    }

    @Transactional
    public void updateQuantity(long cartItemId, User user, int newQuantity){
        CartItem existingItem = cartItemRepo.findByUserAndId(user, cartItemId).orElseThrow(() -> new IllegalArgumentException("No cart item with that id exists"));
        existingItem.setQuantity(newQuantity);
        cartItemRepo.save(existingItem);
    }

    @Transactional
    public void removeFromCart(long cartItemId, User user){
        cartItemRepo.deleteByIdAndUser(cartItemId, user);
    }
}
