package com.example.e_commerce.service;

import com.example.e_commerce.models.Cart;
import com.example.e_commerce.models.CartItem;
import com.example.e_commerce.models.Product;
import com.example.e_commerce.repository.CartItemRepository;
import com.example.e_commerce.repository.CartRepository;
import com.example.e_commerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public Optional<Cart> getCart(Long cartId) {
        return cartRepository.findById(cartId);
    }

    @Transactional
    public Cart addProductToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found."));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found."));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Product stock is not enough.");
        }

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            cart.getCartItems().add(newCartItem);
            cartItemRepository.save(newCartItem);
        }

        calculateCartTotalPrice(cart);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeProductFromCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found."));

        Optional<CartItem> optionalCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (optionalCartItem.isEmpty()) {
            throw new RuntimeException("Product is not in the cart.");
        }

        CartItem cartItem = optionalCartItem.get();

        if (cartItem.getQuantity() <= quantity) {
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() - quantity);
            cartItemRepository.save(cartItem);
        }

        calculateCartTotalPrice(cart);
        return cartRepository.save(cart);
    }

    @Transactional
    public void emptyCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found."));

        cart.getCartItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    private void calculateCartTotalPrice(Cart cart) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem item : cart.getCartItems()) {
            BigDecimal itemPrice = item.getProduct().getPrice();
            BigDecimal itemQuantity = new BigDecimal(item.getQuantity());
            totalPrice = totalPrice.add(itemPrice.multiply(itemQuantity));
        }
        cart.setTotalPrice(totalPrice);
    }
}