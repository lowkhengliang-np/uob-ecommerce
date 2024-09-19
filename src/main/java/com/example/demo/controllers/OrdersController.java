package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.models.Order;
import com.example.demo.models.OrderItem;
import com.example.demo.models.Product;
import com.example.demo.models.User;
import com.example.demo.repo.OrderRepo;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.UserRepo;
import com.example.demo.services.OrderItemService;

import java.util.*;
import java.security.Principal;

@Controller
public class OrdersController {
    private final OrderItemService orderItemService;

    @Autowired
    public OrdersController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping("/orders/{id}")
    public String listOrdersByUser(@PathVariable Long id, Model model, Principal principal) {
        List<Order> orders = orderItemService.findOrdersByUser(principal);
        model.addAttribute("orders", orders); // Send list of orders to the template
        return "order/orders"; // Return orders.html template
    }

    @GetMapping("/items")
    public String listOrderItemsByUser(Model model, Principal principal) {
        List<OrderItem> orderItems = orderItemService.findOrderItemsByUser(principal);
        model.addAttribute("orderItems", orderItems); // Send list of order items to the template
        return "order/orderItems"; // Return orderItems.html template
    }

    // private final OrderRepo orderRepo;
    // private final UserRepo userRepo;
    
    // @Autowired
    // public OrdersController(OrderRepo orderRepo, UserRepo userRepo){
    //     this.orderRepo = orderRepo;
    //     this.userRepo = userRepo;
    // }

    // @GetMapping("/{id}")
    // public String listOrdersByUser(@PathVariable Long id, Model model, Principal principal){
        
    //     // String username = principal.getName();
    //     // User user = userRepo.findByUsername(username);
    //     // List<Order> orders = orderRepo.findByUser(user); //orderService.findByUser()
    //     model.addAttribute("orders", orders); // send list of order info to template
    //     return "/order/orders"; //return orders.html
    // }

    // @GetMapping("")
    // public String listAllOrders(Model model){
    //     List<Order> orders = orderRepo.findAll();
    //     model.addAttribute("orders", orders);
    //     return "/order/orders";
    // }
}

//Service call repo, Service return to controller, Controller populate view model.
//Service is processing -> Pass the data to controller through orderService.serviceMethod() defined in Repo
//Controller passes data to html