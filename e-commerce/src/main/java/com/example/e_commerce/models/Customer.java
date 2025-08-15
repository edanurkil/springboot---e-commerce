package com.example.e_commerce.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.example.e_commerce.models.BaseEntity;
import com.example.e_commerce.models.Cart;
import com.example.e_commerce.models.Order;
import com.example.e_commerce.models.User;

@Entity
@Getter
@Setter
public class Customer extends BaseEntity{

    private String Name;

    private String email;

    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Cart cart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;
}
