package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Order;
import com.example.demo.models.User;
import java.util.*;

public interface OrderRepo extends JpaRepository<Order, Long>{
    List<Order> findAll();
    List<Order> findByUser(User user);
    void deleteByUser(User user);
    void deleteByIdAndUser(long id, User user);
}