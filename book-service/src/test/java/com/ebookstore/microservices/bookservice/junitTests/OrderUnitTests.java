package com.ebookstore.microservices.bookservice.junitTests;

import com.ebookstore.microservices.bookservice.enumerations.Discount;
import com.ebookstore.microservices.bookservice.exceptions.OrderNotFoundException;
import com.ebookstore.microservices.bookservice.models.Cart;
import com.ebookstore.microservices.bookservice.models.Order;
import com.ebookstore.microservices.bookservice.repositories.OrderRepository;
import com.ebookstore.microservices.bookservice.services.CartService;
import com.ebookstore.microservices.bookservice.services.impl.OrderServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class OrderUnitTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllOrders(){
        Order order1 = new Order(1L, new Cart(1L), Discount.MEDIUM);
        Order order2 = new Order(2L, new Cart(2L), Discount.HIGH);
        Order order3 = new Order(3L, new Cart(3L), Discount.LOW);

        List<Order> expectedOrders = Arrays.asList(order1, order2, order3);

        when(orderRepository.findAll()).thenReturn(expectedOrders);

        List<Order> ordersFound = orderService.findAll();

        assertEquals(expectedOrders, ordersFound);
    }

    @Test
    public void testFindOrderByIdFound(){
        Long orderId = 1L;
        Order expectedOrder = new Order(2L, new Cart(2L),Discount.MEDIUM);
        expectedOrder.setOrderId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));

        Order orderFound = orderService.findById(orderId);

        assertEquals(expectedOrder, orderFound);
    }

    @Test
    public void testFindOrderByIdNotFound(){
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.findById(orderId);
        });
    }

    @Test
    public void testFindOrderByCustomerId(){
        Long customerId = 1L;
        Order expectedOrder = new Order(customerId, new Cart(customerId), Discount.LOW);

        when(orderRepository.findOrderByCustomerId(customerId)).thenReturn(expectedOrder);

        Order orderFound = orderService.findByCustomerId(customerId);

        assertEquals(expectedOrder, orderFound);
    }

    @Test
    public void testCreateOrder(){
        Order expectedOrder = new Order(1L, new Cart(1L),Discount.LOW);

        when(orderRepository.save(expectedOrder)).thenReturn(expectedOrder);

        Order orderCreated = orderService.create(expectedOrder);

        assertEquals(expectedOrder, orderCreated);
    }

    @Test
    public void testDeleteOrderById(){
        Long orderId = 1L;

        orderService.deleteById(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    public void testCalcualtePriceWithLowDiscount(){
        double totalPrice = 100.00;
        Discount discount = Discount.LOW;

        double discountedPrice = orderService.calculateDiscountOnTotalPrice(totalPrice, discount);

        assertEquals(90.0, discountedPrice);
    }

    @Test
    public void testCalcualtePriceWithMediumDiscount(){
        double totalPrice = 100.00;
        Discount discount = Discount.MEDIUM;

        double discountedPrice = orderService.calculateDiscountOnTotalPrice(totalPrice, discount);

        assertEquals(70.0, discountedPrice);
    }

    @Test
    public void testCalcualtePriceWithHighDiscount(){
        double totalPrice = 100.00;
        Discount discount = Discount.HIGH;

        double discountedPrice = orderService.calculateDiscountOnTotalPrice(totalPrice, discount);

        assertEquals(50.0, discountedPrice);
    }

    @Test
    public void testCalcualtePriceWithNoDiscount(){
        double totalPrice = 100.00;

        double discountedPrice = orderService.calculateDiscountOnTotalPrice(totalPrice, null);

        assertEquals(100.0, discountedPrice);
    }
}
