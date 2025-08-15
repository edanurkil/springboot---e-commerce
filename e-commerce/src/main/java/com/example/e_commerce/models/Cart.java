package com.example.e_commerce.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
public class Cart extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private BigDecimal totalPrice = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;
}
