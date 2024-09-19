package com.example.demo.services;

import java.security.Principal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.OrderItem;
import com.example.demo.models.User;
import com.example.demo.models.Order;
import com.example.demo.repo.OrderRepo;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.UserRepo;

import jakarta.transaction.Transactional;

//Processing
@Service
public class OrderItemService {
    private final OrderRepo orderRepo;
    private final UserRepo userRepo;

    @Autowired
    public OrderItemService(OrderRepo orderRepo, UserRepo userRepo){
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
    }

    //Get all orders (not in use)
    public List<Order> findAllOrder(){
        return orderRepo.findAll();
    }
    //Get all order items (not in use)
    public List<OrderItem> findAllOrderItems(List<Order> orders){
        List<OrderItem> orderItems = new ArrayList<>();
        for (Order order : orders) {
            orderItems.addAll(order.getOrderItems());
        }
        return orderItems;
    }

    private User getUserFromPrincipal(Principal principal) {
        if (principal == null) {
            return null;
        }
        String username = principal.getName();
        return userRepo.findByUsername(username);
    }

    // Get orders from a user
    public List<Order> findOrdersByUser(Principal principal) {
        User user = getUserFromPrincipal(principal);
        return user != null ? orderRepo.findByUser(user) : Collections.emptyList();
    }

    // Get order items from a user
    public List<OrderItem> findOrderItemsByUser(Principal principal) {
        List<Order> orders = findOrdersByUser(principal);
        return findAllOrderItems(orders);
    }

    @Transactional
    public void removeOrder(long order_id, User user){
        orderRepo.deleteByIdAndUser(order_id, user);
    }

}
