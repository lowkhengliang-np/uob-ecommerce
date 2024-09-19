package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.OrderItem;
import java.util.*;

public interface OrderItemRepo extends JpaRepository<OrderItem, Long>{
    List<OrderItem> findAll();
}
