package com.appsdeveloperblog.products.dao.jpa.repository;

import com.appsdeveloperblog.products.dao.jpa.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
