package com.example.e_commerce.service;

import com.example.e_commerce.models.Product;
import com.example.e_commerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setStock(updatedProduct.getStock());
            return productRepository.save(existingProduct);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            productRepository.delete(optionalProduct.get());
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }
}