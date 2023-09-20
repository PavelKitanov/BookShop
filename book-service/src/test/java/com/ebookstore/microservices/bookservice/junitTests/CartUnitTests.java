package com.ebookstore.microservices.bookservice.junitTests;

import com.ebookstore.microservices.bookservice.exceptions.CartNotFoundException;
import com.ebookstore.microservices.bookservice.models.*;
import com.ebookstore.microservices.bookservice.proxy.AuthenticationProxy;
import com.ebookstore.microservices.bookservice.repositories.BookRepository;
import com.ebookstore.microservices.bookservice.repositories.CartItemRepository;
import com.ebookstore.microservices.bookservice.repositories.CartRepository;
import com.ebookstore.microservices.bookservice.services.impl.CartServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class CartUnitTests {

    @Mock
    private AuthenticationProxy authenticationProxy;
    @Mock
    private  CartRepository cartRepository;
    @Mock
    private  BookRepository bookRepository;
    @Mock
    private  CartItemRepository cartItemRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCarts(){
        Cart cart1 = new Cart(1L);
        Cart cart2 = new Cart(2L);
        List<Cart> expectedCarts = Arrays.asList(cart1, cart2);

        when(cartRepository.findAll()).thenReturn(expectedCarts);

        List<Cart> actualCarts = cartService.findAll();

        assertEquals(expectedCarts.size(), actualCarts.size());
        assertEquals(expectedCarts, actualCarts);
    }

    @Test
    public void testFindCartByIdFound(){
        Long cartId = 1L;
        Cart expectedCart = new Cart(2L);
        expectedCart.setCartId(cartId);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(expectedCart));

        Cart cartFound = cartService.getCartById(cartId);

        assertEquals(expectedCart, cartFound);
    }

    @Test
    public void testFindCartByIdNotFound(){
        Long cartId = 1L;

        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> {
            cartService.getCartById(cartId);
        });
    }

    @Test
    public void testFindCartByCustomerIdFound(){
        Long customerId = 1L;
        Cart expectedCart = new Cart(customerId);

        when(cartRepository.getCartByCustomerId(customerId)).thenReturn(expectedCart);

        Cart cartFound = cartService.getCartByCustomerId(customerId);

        assertEquals(expectedCart, cartFound);
    }


    @Test
    public void testCreateCart(){
        Cart cartToCreate = new Cart(1L);

        when(cartRepository.save(cartToCreate)).thenReturn(cartToCreate);

        Cart savedCart = cartService.create(cartToCreate);

        assertEquals(cartToCreate, savedCart);
    }

    @Test
    public void testAddItemToCart(){
        Long customerId = 1L;
        Long bookId = 2L;
        int quantity = 5;

        Cart cart = new Cart(customerId);
        Book book = new Book("Book title", new Author("John","Doe"), new Genre("Fiction"), "description", 5.99);
        CartItem cartItem = new CartItem(book, quantity);

        when(cartRepository.getCartByCustomerId(customerId)).thenReturn(cart);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart updatedCart = cartService.addItemToCart(customerId, bookId, quantity);

        assertEquals(quantity, updatedCart.getCartItems().get(0).getQuantity());
        assertEquals(book, updatedCart.getCartItems().get(0).getBook());
        assertEquals(1, updatedCart.getCartItems().size());
    }

    @Test
    public void testRemoveItemFromCart(){
        Long customerId = 1L;
        Long item1Id = 2L;
        Long item2Id= 3L;
        int quantity = 5;

        Cart cart = new Cart(customerId);
        Book book1 = new Book("Book1", new Author("John","Doe"), new Genre("Fiction"), "description", 5.99);
        Book book2 = new Book("Book2", new Author("Jane","Smith"), new Genre("Non-Fiction"), "another description", 8.99);
        CartItem cartItem1 = new CartItem(book1, quantity);
        CartItem cartItem2 = new CartItem(book2, quantity);
        cart.getCartItems().add(cartItem1);
        cart.getCartItems().add(cartItem2);

        when(cartRepository.getCartByCustomerId(customerId)).thenReturn(cart);
        when(cartItemRepository.findById(item1Id)).thenReturn(Optional.of(cartItem1));
        when(cartRepository.save(cart)).thenReturn(cart);

        Cart updatedCart = cartService.removeItemFromCart(customerId, item1Id);

        assertEquals(1, updatedCart.getCartItems().size());
        assertEquals(cartItem2, updatedCart.getCartItems().get(0));
    }

    @Test
    public void testDeleteCartById(){
        Long cartId = 1L;

        cartService.deleteById(cartId);

        verify(cartRepository, times(1)).deleteById(cartId);
    }

}
