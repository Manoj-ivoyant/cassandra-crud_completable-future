package com.ivoyant.cassandracrud.controller;

import com.ivoyant.cassandracrud.model.Product;
import com.ivoyant.cassandracrud.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public Product save(@RequestBody Product product) {
        return productService.save(product);
    }

    @GetMapping("/saveAll")
    public String saveAllFromJson() {
        return productService.someOtherMethod();
    }

    @GetMapping
    public List<Product> getAll() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Integer id) {
        return productService.getById(id);
    }

    @PutMapping("/{id}")
    public String updateById(@PathVariable Integer id, @RequestBody Product product) {
        return productService.updateById(id, product);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        return productService.deleteById(id);
    }

    @GetMapping("/filterCond")
    public List<Product> getProductsByFilter(){
        return productService.detailsOfProduct();
    }
}
