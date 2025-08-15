package com.example.e_commerce.controllers;

import com.example.e_commerce.models.Cart;
import com.example.e_commerce.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable Long id) {
        Optional<Cart> cart = cartService.getCart(id);
        return cart.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{cartId}/products/{productId}")
    public ResponseEntity<Cart> addProductToCart(@PathVariable Long cartId, @PathVariable Long productId, @RequestParam int quantity) {
        try {
            Cart updatedCart = cartService.addProductToCart(cartId, productId, quantity);
            return new ResponseEntity<>(updatedCart, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{cartId}/products/{productId}")
    public ResponseEntity<Cart> removeProductFromCart(@PathVariable Long cartId, @PathVariable Long productId, @RequestParam int quantity) {
        try {
            Cart updatedCart = cartService.removeProductFromCart(cartId, productId, quantity);
            return new ResponseEntity<>(updatedCart, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> emptyCart(@PathVariable Long id) {
        try {
            cartService.emptyCart(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}