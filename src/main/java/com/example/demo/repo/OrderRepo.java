package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.models.Order;
import com.example.demo.models.User;

import jakarta.transaction.Transactional;

import java.util.*;

public interface OrderRepo extends JpaRepository<Order, Long>{
    List<Order> findAll();
    List<Order> findByUser(User user);
    void deleteByUser(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Order o WHERE o.id = :id AND o.user = :user")
    void deleteByIdAndUser(long id, User user);

}