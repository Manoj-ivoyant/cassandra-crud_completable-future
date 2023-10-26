package com.ivoyant.cassandracrud.repository;

import com.ivoyant.cassandracrud.model.Product;
import org.springframework.data.cassandra.repository.CassandraRepository;


public interface ProductRepository extends CassandraRepository<Product, Integer> {
}
