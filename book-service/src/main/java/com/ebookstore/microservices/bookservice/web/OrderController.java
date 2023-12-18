package com.ebookstore.microservices.bookservice.web;

import com.ebookstore.microservices.bookservice.dto.UserDto;
import com.ebookstore.microservices.bookservice.exceptions.NoItemsInCartException;
import com.ebookstore.microservices.bookservice.payload.PaymentConfirmationRequest;
import com.ebookstore.microservices.bookservice.enumerations.Discount;
import com.ebookstore.microservices.bookservice.models.Cart;
import com.ebookstore.microservices.bookservice.models.Order;
import com.ebookstore.microservices.bookservice.payload.StripeChargeRequest;
import com.ebookstore.microservices.bookservice.proxy.PaymentProxy;
import com.ebookstore.microservices.bookservice.services.CartService;
import com.ebookstore.microservices.bookservice.services.OrderService;
import com.ebookstore.microservices.bookservice.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private final TokenService tokenService;
    @Autowired
    private final PaymentProxy paymentProxy;
    @Autowired
    private final OrderService orderService;
    @Autowired
    private final CartService cartService;

    public OrderController(TokenService tokenService, PaymentProxy paymentProxy, OrderService orderService, CartService cartService) {
        this.tokenService = tokenService;
        this.paymentProxy = paymentProxy;
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> findAll(@RequestHeader("Authorization") String tokenHeader){
        tokenService.callValidateToken(tokenHeader);
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Order> getOrderById(@RequestHeader("Authorization") String tokenHeader,
                          @PathVariable Long orderId){
        tokenService.callValidateToken(tokenHeader);

        Order order = orderService.findById(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/ordersByCustomer")
    public ResponseEntity<List<Order>> getOrdersByCustomer(@RequestHeader("Authorization") String tokenHeader){
        ResponseEntity<UserDto> authResponse = tokenService.callValidateToken(tokenHeader);
        UserDto userDto = authResponse.getBody();

        List<Order> orders = orderService.findOrdersByCustomerId(userDto.getUserId());
        Collections.reverse(orders);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/createOrder")
    public ResponseEntity<Order> createOrder(@RequestHeader("Authorization") String tokenHeader,
                                             @RequestParam(required = false) Discount discount,
                                             @RequestBody StripeChargeRequest stripeChargeRequest){
        ResponseEntity<UserDto> authResponse = tokenService.callValidateToken(tokenHeader);
        UserDto userDto = authResponse.getBody();

        Cart cart = cartService.getCartByCustomerId(userDto.getUserId());
        double orderTotalPrice = orderService.calculateDiscountOnTotalPrice(cart.getCartTotalPrice(), discount);

        if(orderTotalPrice == 0)
            throw new NoItemsInCartException("There are no items in a cart with id " + cart.getCartId());

        paymentProxy.charge(stripeChargeRequest);

        return ResponseEntity.ok(orderService.create(userDto.getUserId(), cart.getCartId(), discount));
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<Void> removeOrder(@RequestHeader("Authorization") String tokenHeader,
                            @PathVariable Long orderId){
        tokenService.callValidateToken(tokenHeader);
        orderService.deleteById(orderId);
        return ResponseEntity.noContent().build();
    }
}
