package com.ebookstore.microservices.bookservice.web;

import com.ebookstore.microservices.bookservice.payload.PaymentConfirmationRequest;
import com.ebookstore.microservices.bookservice.enumerations.Discount;
import com.ebookstore.microservices.bookservice.models.Cart;
import com.ebookstore.microservices.bookservice.models.Order;
import com.ebookstore.microservices.bookservice.proxy.PaymentProxy;
import com.ebookstore.microservices.bookservice.services.CartService;
import com.ebookstore.microservices.bookservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private final PaymentProxy paymentProxy;
    @Autowired
    private final OrderService orderService;
    @Autowired
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

    @GetMapping("/order/{orderid}")
    public Order findById(@PathVariable Long orderId){
        return orderService.findById(orderId);
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
            ResponseEntity<String> responseEntity = paymentProxy.confirmPayment(request);
            String response = responseEntity.getBody();
        }

        return orderService.create(customerId,cartId, discount);
    }

    @DeleteMapping("/delete/{orderId}")
    public void removeOrder(@PathVariable Long orderId){
        orderService.deleteById(orderId);
    }
}
