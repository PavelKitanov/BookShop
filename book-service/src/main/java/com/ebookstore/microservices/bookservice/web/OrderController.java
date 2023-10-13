package com.ebookstore.microservices.bookservice.web;

import com.ebookstore.microservices.bookservice.dto.UserDto;
import com.ebookstore.microservices.bookservice.exceptions.NoItemsInCartException;
import com.ebookstore.microservices.bookservice.payload.PaymentConfirmationRequest;
import com.ebookstore.microservices.bookservice.enumerations.Discount;
import com.ebookstore.microservices.bookservice.models.Cart;
import com.ebookstore.microservices.bookservice.models.Order;
import com.ebookstore.microservices.bookservice.proxy.PaymentProxy;
import com.ebookstore.microservices.bookservice.services.CartService;
import com.ebookstore.microservices.bookservice.services.OrderService;
import com.ebookstore.microservices.bookservice.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/order/orderByCustomer/{customerId}")
    public ResponseEntity<Order> getOrderByCustomer(@RequestHeader("Authorization") String tokenHeader,
                                    @PathVariable Long customerId){
        tokenService.callValidateToken(tokenHeader);

        Order order = orderService.findByCustomerId(customerId);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/createOrder")
    public ResponseEntity<Order> createOrder(@RequestHeader("Authorization") String tokenHeader,
                             @RequestParam(required = false) Discount discount
                             ){
        ResponseEntity<UserDto> authResponse = tokenService.callValidateToken(tokenHeader);
        UserDto userDto = authResponse.getBody();

        Cart cart = cartService.getCartByCustomerId(userDto.getUserId());
        double orderTotalPrice = orderService.calculateDiscountOnTotalPrice(cart.getCartTotalPrice(), discount);

        if(orderTotalPrice == 0)
            throw new NoItemsInCartException("There are no items in a cart with id " + cart.getCartId());

        PaymentConfirmationRequest request = paymentProxy.createPaymentIntent(orderTotalPrice);

        if(request != null){
            paymentProxy.confirmPayment(request);
        }

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
