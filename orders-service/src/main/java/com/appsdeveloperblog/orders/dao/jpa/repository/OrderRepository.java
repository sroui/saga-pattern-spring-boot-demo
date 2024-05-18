package com.appsdeveloperblog.orders.dao.jpa.repository;

import com.appsdeveloperblog.orders.dao.jpa.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
