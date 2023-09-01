package com.ebookstore.microservices.bookservice.web;

import com.ebookstore.microservices.bookservice.dto.PaymentConfirmationRequest;
import com.ebookstore.microservices.bookservice.enumerations.Discount;
import com.ebookstore.microservices.bookservice.models.Cart;
import com.ebookstore.microservices.bookservice.models.Order;
import com.ebookstore.microservices.bookservice.proxy.PaymentProxy;
import com.ebookstore.microservices.bookservice.services.CartService;
import com.ebookstore.microservices.bookservice.services.OrderService;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private final PaymentProxy paymentProxy;
    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(PaymentProxy paymentProxy, OrderService orderService, CartService cartService) {
        this.paymentProxy = paymentProxy;
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping
    public List<Order> findAll(){
        return orderService.findAll();
    }

    @GetMapping("/order/{id}")
    public Order findById(@PathVariable Long id){
        return orderService.findById(id);
    }

    @GetMapping("/order/orderByCustomer/{customerId}")
    public Order getOrderByCustomer(@PathVariable Long customerId){
        return orderService.findByCustomerId(customerId);
    }

    @PostMapping("/createOrder")
    public Order createOrder(@RequestParam Long customerId,
                             @RequestParam Long cartId,
                             @RequestParam(required = false) Discount discount
                             ){

        Cart cart = cartService.getCartById(cartId);
        double orderTotalPrice = orderService.calculateDiscountOnTotalPrice(cart.getCartTotalPrice(), discount);

        PaymentConfirmationRequest request = paymentProxy.createPaymentIntent(orderTotalPrice);

        if(request != null){
            paymentProxy.confirmPayment(request);
        }

        return orderService.create(customerId,cartId, discount);
    }
}
