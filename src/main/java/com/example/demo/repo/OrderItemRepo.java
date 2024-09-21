package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.OrderItem;

import jakarta.transaction.Transactional;

import java.util.*;

public interface OrderItemRepo extends JpaRepository<OrderItem, Long>{
    List<OrderItem> findAll();

    @Transactional
    @Modifying
    @Query("DELETE FROM OrderItem oi WHERE oi.order.id = :orderId")
    void deleteByOrderId(@Param("orderId") Long orderId);
}
