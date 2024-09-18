package com.example.demo.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.models.CartItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.annotation.PostConstruct;

import java.math.BigDecimal;
import java.util.*;

@Service
public class StripeService {
    @Value("${stripe.api.secretKey}")
    private String stripeSecretKey;
    @Value("${stripe.api.publicKey}")
    private String stripePublicKey;

    @PostConstruct
    public void init(){
        Stripe.apiKey = stripeSecretKey;
    }

    public Session createCheckout (List<CartItem> cartItems, String successUrl, String cancelURL)
        throws StripeException{
        // Create line items, description of the product + quantity + price per unit
        //pass all the line items, along with payment requirements(like currency) to stripe
        // receive the checkout session id from stripe
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        for (CartItem item: cartItems){
            //line item consists product price quantity
            var productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
            .setName(item.getProduct().getName()).putMetadata("product_id", item.getProduct().getId().toString()).build();

            //consists of unit amount and currency and product info
            var priceData = SessionCreateParams.LineItem.PriceData.builder().setCurrency("usd")
            .setUnitAmount(item.getProduct().getPrice().multiply(new BigDecimal("100")).longValue())
            .setProductData(productData).build();
            //line item conssits of quantity and the price data
            var lineItem = SessionCreateParams.LineItem.builder().setPriceData(priceData)
            .setQuantity((long)item.getQuantity()).build();
            lineItems.add(lineItem);
        }
        //create checkout session
        SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
        .setCancelUrl(cancelURL).setSuccessUrl(successUrl).addAllLineItem(lineItems).build();

        return Session.create(params);
        
    }

    public String getPublicKey(){
        return stripePublicKey;
    }
}
