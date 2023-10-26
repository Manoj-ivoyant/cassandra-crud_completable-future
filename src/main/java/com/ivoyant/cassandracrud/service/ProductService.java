package com.ivoyant.cassandracrud.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivoyant.cassandracrud.model.Product;
import com.ivoyant.cassandracrud.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ObjectMapper objectMapper=new ObjectMapper();
    @Autowired
    private ProductRepository productRepository;


    public Product save(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("product not found"));
    }

    public String updateById(Integer id, Product product) {
        Product product1 = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product1.setPrice(product.getPrice());
        product1.setQuantity(product.getQuantity());
        return "updation successfully";
    }

    public String deleteById(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
        return "success";
    }

    public CompletableFuture<List<Product>> saveAllByJsonAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Product> products = objectMapper.readValue(new File("product.json"), new TypeReference<List<Product>>() {});
                return products;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public String someOtherMethod() {
        CompletableFuture<List<Product>> future = saveAllByJsonAsync();
        List<Product> products = future.join().stream().toList();
        productRepository.saveAll(products);
        return new String("success");
    }


    //chaining using
    public List<Product> detailsOfProduct() {
        ExecutorService executors=Executors.newFixedThreadPool(10);
        CompletableFuture<List<Product>> listCompletableFuture = CompletableFuture.supplyAsync(() -> {
            logger.info("Fetching product by: " + Thread.currentThread().getName());
            return productRepository.findAll();
        },executors);

        CompletableFuture<List<Product>> filteredProductsFuture = listCompletableFuture.thenApply(products -> {
            logger.info("Filter products having quantity less than 10: " + Thread.currentThread().getName());
            return products.stream()
                    .filter(product -> product.getQuantity() < 10)
                    .collect(Collectors.toList());
        }).thenApplyAsync(filteredProducts -> {
            logger.info("Filter products by price less than 200: " + Thread.currentThread().getName());
            return filteredProducts.stream()
                    .filter(product -> product.getPrice() < 200)
                    .collect(Collectors.toList());
        });

        try {
            return filteredProductsFuture.get().stream().toList(); // it is Blocking until all processing is complete.
        } catch (InterruptedException | ExecutionException e) {
            // Handls exceptions as needed.
            e.printStackTrace();
            return Collections.emptyList(); // Return an empty list as a default.
        }
    }
}
