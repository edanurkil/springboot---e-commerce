package com.example.e_commerce.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Address extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    public Address shippingAddress;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    public Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    public Product product;


}
