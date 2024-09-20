package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.Order;
import com.example.demo.models.OrderItem;
import com.example.demo.models.Product;
import com.example.demo.models.User;
import com.example.demo.repo.OrderRepo;
import com.example.demo.repo.UserRepo;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.OrderItemRepo;
import com.example.demo.services.OrderItemService;
import com.example.demo.services.UserService;
import com.example.demo.services.OrderService;

import java.util.*;
import java.security.Principal;

@Controller
public class OrdersController {
    private final OrderItemService orderItemService;
    private final UserService userService;
    private final OrderService orderService;
    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final OrderItemRepo orderItemRepo;

    @Autowired
    public OrdersController(OrderItemService orderItemService, UserRepo userRepo, UserService userService, OrderService orderService, OrderRepo orderRepo, OrderItemRepo orderItemRepo) {
        this.orderItemService = orderItemService;
        this.userService = userService;
        this.orderService = orderService;
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.orderItemRepo = orderItemRepo;

    }

    @GetMapping("/orders")
    public String listOrdersByUser(Model model, Principal principal) {
        List<Order> orders = orderItemService.findOrdersByUser(principal);
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        String email = user.getEmail();

        // List<String> status = orderService.getOrderStatus(orders);
        model.addAttribute("orders", orders); // Send list of orders to the template
        model.addAttribute("username", username);
        model.addAttribute("email", email);
        // model.addAttribute("status", status);
        return "order/orders"; // Return orders.html template
    }

    @GetMapping("/items")
    public String listOrderItemsByUser(Model model, Principal principal) {
        List<OrderItem> orderItems = orderItemService.findOrderItemsByUser(principal);
        model.addAttribute("orderItems", orderItems); // Send list of order items to the template
        return "order/orderItems"; // Return orderItems.html template
    }

    //Deleting
    @GetMapping("/order/{id}/delete")
    public String showDeleteProductForm(@PathVariable Long id, Model model){
        var orderDel = orderRepo.findById(id).orElseThrow(()-> new RuntimeException());
        model.addAttribute("orderDel", orderDel);
        return "order/delete";
    } 
    // Process delete
    @PostMapping("/order/{id}/delete")
    public String deleteOrder(@PathVariable Long id, Principal principal){
        orderItemRepo.deleteByOrderId(id); //Clear the order items associated to order id first
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        orderRepo.deleteByIdAndUser(id, user);
        return "redirect:/orders";
    }

    // @PostMapping("/order/{id}/delete")
    // public String removeFromCart(@PathVariable long id, Principal principal, RedirectAttributes redirectAttributes){
    //     try {
    //         var user = userService.findUserByUsername(principal.getName());
    //         orderService.removeFromOrder(id, user);
    //         redirectAttributes.addFlashAttribute("message", "The order has been deleted");
    //     } catch (Exception e) {
    //         redirectAttributes.addFlashAttribute("message", e.getMessage());
    //     }
    //     return "redirect:/orders";
    // }


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