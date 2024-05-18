package com.appsdeveloperblog.payments.dao.jpa.repository;

import com.appsdeveloperblog.payments.dao.jpa.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
