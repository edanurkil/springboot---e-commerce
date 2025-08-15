package com.example.e_commerce.repository;

import com.example.e_commerce.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
