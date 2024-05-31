package com.appsdeveloperblog.payments.service;

import com.appsdeveloperblog.core.dto.Payment;
import com.appsdeveloperblog.core.dto.events.PaymentProcessedEvent;
import com.appsdeveloperblog.payments.dao.jpa.entity.PaymentEntity;
import com.appsdeveloperblog.payments.dao.jpa.repository.PaymentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String paymentsEventsTopicName;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              KafkaTemplate<String, Object> kafkaTemplate,
                              @Value("${payments.events.topic.name}") String paymentsEventsTopicName) {
        this.paymentRepository = paymentRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.paymentsEventsTopicName = paymentsEventsTopicName;
    }

    @Override
    public void process(Payment payment) {
        if (isValid(payment)) {
            PaymentEntity paymentEntity = new PaymentEntity();
            BeanUtils.copyProperties(payment, paymentEntity);
            paymentRepository.save(paymentEntity);

            var paymentProcessedEvent = new PaymentProcessedEvent(payment.getOrderId(), paymentEntity.getId());
            kafkaTemplate.send(paymentsEventsTopicName, paymentProcessedEvent);
        } else {
            // todo send payment failed event
        }
    }

    @Override
    public boolean isValid(Payment payment) {
        return true; //todo
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll().stream().map(entity -> new Payment(entity.getId(), entity.getOrderId(), entity.getProductId(), entity.getProductPrice(), entity.getProductQuantity())
        ).collect(Collectors.toList());
    }
}
