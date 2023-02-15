package com.senior.api.UserConsumption.repository;

import com.senior.api.UserConsumption.domain.ProductService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductServiceRepository extends JpaRepository<ProductService, Long> {
}
