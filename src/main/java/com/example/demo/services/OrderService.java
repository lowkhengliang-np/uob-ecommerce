package com.example.demo.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.LineItem;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionListLineItemsParams;

@Service
public class OrderService {
    
    public void handleSuccessfulPayment(Event event){
        System.out.println("STRIPE API VERSION = " + Stripe.API_VERSION);

        // Get the session
        Session session = (Session) event.getDataObjectDeserializer().getObject().get();
        String sessionId = session.getId();
        try{
            //expand each line item
            SessionListLineItemsParams listItemParams = SessionListLineItemsParams.builder()
                                                        .addExpand("data.price.product").build();
            
            //get the data for each of them
            List<LineItem> lineItems = session.listLineItems(listItemParams).getData();
            //Store a dictionary of product_id to quantity
            Map<String, Long> orderedProducts = new HashMap<>();


            for (LineItem item: lineItems){
                String productId = item.getPrice().getProductObject().getMetadata().get("product_id");
                if (productId == null || productId.isEmpty()){
                    System.out.println("Unable to get product id for line item" + item);
                    continue; // skip the rest of the lines in the loop and start a new iteration
                }
                long quantity = item.getQuantity();
                orderedProducts.put(productId, quantity);
            }
            System.out.println(orderedProducts);
        }
        catch(StripeException e){
            System.out.println(e);
        }
    }
}
