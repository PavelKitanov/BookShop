package com.ebookstore.microservices.bookservice.web;

import com.ebookstore.microservices.bookservice.dto.UserDto;
import com.ebookstore.microservices.bookservice.exceptions.NoItemsInCartException;
import com.ebookstore.microservices.bookservice.exceptions.OrderNotFoundException;
import com.ebookstore.microservices.bookservice.payload.PaymentConfirmationRequest;
import com.ebookstore.microservices.bookservice.enumerations.Discount;
import com.ebookstore.microservices.bookservice.models.Cart;
import com.ebookstore.microservices.bookservice.models.Order;
import com.ebookstore.microservices.bookservice.proxy.AuthenticationProxy;
import com.ebookstore.microservices.bookservice.proxy.PaymentProxy;
import com.ebookstore.microservices.bookservice.services.CartService;
import com.ebookstore.microservices.bookservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private final AuthenticationProxy authenticationProxy;
    @Autowired
    private final PaymentProxy paymentProxy;
    @Autowired
    private final OrderService orderService;
    @Autowired
    private final CartService cartService;

    public OrderController(AuthenticationProxy authenticationProxy, PaymentProxy paymentProxy, OrderService orderService, CartService cartService) {
        this.authenticationProxy = authenticationProxy;
        this.paymentProxy = paymentProxy;
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> findAll(@RequestHeader("Authorization") String tokenHeader){
        authenticationProxy.validateToken(tokenHeader);
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/order/{orderid}")
    public ResponseEntity<?> getOrderById(@RequestHeader("Authorization") String tokenHeader,
                          @PathVariable Long orderId){
        authenticationProxy.validateToken(tokenHeader);
        try {
           Order order = orderService.findById(orderId);
           return ResponseEntity.ok(order);
        }
        catch (OrderNotFoundException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @GetMapping("/order/orderByCustomer/{customerId}")
    public ResponseEntity<?> getOrderByCustomer(@RequestHeader("Authorization") String tokenHeader,
                                    @PathVariable Long customerId){
        authenticationProxy.validateToken(tokenHeader);
        try{
            Order order = orderService.findByCustomerId(customerId);
            return ResponseEntity.ok(order);
        }
        catch (OrderNotFoundException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }

    }

    @PostMapping("/createOrder")
    public ResponseEntity<Order> createOrder(@RequestHeader("Authorization") String tokenHeader,
                             @RequestParam(required = false) Discount discount
                             ){
        ResponseEntity<UserDto> authResponse = authenticationProxy.validateToken(tokenHeader);
        UserDto userDto = authResponse.getBody();

        Cart cart = cartService.getCartByCustomerId(userDto.getUserId());
        double orderTotalPrice = orderService.calculateDiscountOnTotalPrice(cart.getCartTotalPrice(), discount);

        if(orderTotalPrice == 0)
            throw new NoItemsInCartException("There are no items in a cart with id " + cart.getCartId());

        PaymentConfirmationRequest request = paymentProxy.createPaymentIntent(orderTotalPrice);

        if(request != null){
            ResponseEntity<String> responseEntity = paymentProxy.confirmPayment(request);
            String response = responseEntity.getBody();
        }

        return ResponseEntity.ok(orderService.create(userDto.getUserId(), cart.getCartId(), discount));
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<Void> removeOrder(@RequestHeader("Authorization") String tokenHeader,
                            @PathVariable Long orderId){
        authenticationProxy.validateToken(tokenHeader);
        orderService.deleteById(orderId);
        return ResponseEntity.noContent().build();
    }
}
