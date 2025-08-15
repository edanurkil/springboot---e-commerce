package com.example.e_commerce.service;

import com.example.e_commerce.models.*;
import com.example.e_commerce.repository.CartRepository;
import com.example.e_commerce.repository.OrderRepository;
import com.example.e_commerce.repository.OrderItemRepository;
import com.example.e_commerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order placeOrder(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found."));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cannot place an order with an empty cart.");
        }

        Order order = new Order();
        order.setCustomer(cart.getCustomer());
        order.setTotalPrice(cart.getTotalPrice());
        order.setOrderCode(UUID.randomUUID().toString());

        List<OrderItem> orderItems = cart.getCartItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Product '" + product.getName() + "' stock is insufficient.");
            }

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtTimeOfOrder(product.getPrice());

            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        cart.getCartItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);

        return savedOrder;
    }

    public Optional<Order> getOrderForCode(String orderCode) {
        return orderRepository.findByOrderCode(orderCode);
    }

    public List<Order> getAllOrdersForCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
}