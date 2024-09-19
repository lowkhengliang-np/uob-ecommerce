package com.example.demo.services;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Order;
import com.example.demo.models.User;
import com.example.demo.models.OrderItem;
import com.example.demo.models.Product;
import com.example.demo.repo.OrderRepo;
import com.example.demo.repo.OrderItemRepo;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.UserRepo;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.LineItem;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionListLineItemsParams;

@Service
public class OrderService {
    //added
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final OrderItemRepo orderItemRepo;
    private final UserRepo userRepo;

    //added
    @Autowired
    public OrderService(OrderRepo orderRepo, ProductRepo productRepo, OrderItemRepo orderItemRepo, UserRepo userRepo) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.orderItemRepo = orderItemRepo;
        this.userRepo = userRepo;
    }


    public void handleSuccessfulPayment(Event event){
        System.out.println("STRIPE API VERSION = " + Stripe.API_VERSION);

        // Get the session
        Session session = (Session) event.getDataObjectDeserializer().getObject().get();
        String sessionId = session.getId();
        long userId = Long.valueOf(session.getClientReferenceId());
        System.out.println("UserId" + userId);

        try{
            //expand each line item
            SessionListLineItemsParams listItemParams = SessionListLineItemsParams.builder()
                                                        .addExpand("data.price.product").build();
            
            //get the data for each of them
            List<LineItem> lineItems = session.listLineItems(listItemParams).getData();
            
            //Create a new order for the user
            User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
            Order order = new Order();
            order.setUser(user);

            //Store a set of orderItem object
            Set<OrderItem> orderItems = new HashSet<>();
            //Store a dictionary of product_id to quantity
            Map<String, Long> orderedProducts = new HashMap<>();

            for (LineItem item: lineItems){
                String productId = item.getPrice().getProductObject().getMetadata().get("product_id");
                if (productId == null || productId.isEmpty()){
                    System.out.println("Unable to get product id for line item" + item);
                    continue; // skip the rest of the lines in the loop and start a new iteration
                }

                //Added
                Product product = productRepo.findById(Long.valueOf(productId))
                        .orElseThrow(() -> new RuntimeException("Product not found!"));
                //Added

                long quantity = item.getQuantity();
                //Added
                BigDecimal unitPrice = item.getPrice().getUnitAmountDecimal();
                OrderItem orderItem = new OrderItem(order, product, (int) quantity, unitPrice);
                orderRepo.save(order);
                orderItemRepo.save(orderItem);

                System.out.println("Order and Order items saved successfully");
                //Added
                orderedProducts.put(productId, quantity);
            }
            System.out.println(orderedProducts);
        }
        catch(StripeException e){
            System.out.println(e);
        }
    }
}
