package com.example.e_commerce.controllers;


import com.example.e_commerce.models.Order;
import com.example.e_commerce.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place/{cartId}")
    public ResponseEntity<Order> placeOrder(@PathVariable Long cartId) {
        try {
            Order order = orderService.placeOrder(cartId);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/code/{orderCode}")
    public ResponseEntity<Order> getOrderForCode(@PathVariable String orderCode) {
        Optional<Order> order = orderService.getOrderForCode(orderCode);
        return order.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getAllOrdersForCustomer(@PathVariable Long customerId) {
        List<Order> orders = orderService.getAllOrdersForCustomer(customerId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}