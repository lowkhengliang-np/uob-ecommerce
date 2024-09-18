package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.CartItem;
import com.example.demo.models.Product;
import com.example.demo.models.User;

import java.util.*;

public interface CartItemRepo extends JpaRepository<CartItem,Long>{
    // find all the CART item that belongs to a user
    // find by = select * from user
    // by user = where user_id = ?
    List<CartItem> findByUser(User user);

    //findby = select *
    //and = and product = ?
    Optional<CartItem> findByUserAndProduct(User user, Product product);
    Optional<CartItem> findByUserAndId(User user, Long id);
    long countByUser(User user);
    void deleteByUser(User user);
    void deleteByIdAndUser(long id, User user);
}
