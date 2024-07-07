package com.appsdeveloperblog.payments.service.handler;

import com.appsdeveloperblog.core.dto.Payment;
import com.appsdeveloperblog.core.dto.commands.ProcessPaymentCommand;
import com.appsdeveloperblog.core.dto.events.PaymentFailedEvent;
import com.appsdeveloperblog.core.dto.events.PaymentProcessedEvent;
import com.appsdeveloperblog.core.exceptions.CreditCardProcessorUnavailableException;
import com.appsdeveloperblog.payments.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${payments.commands.topic.name}")
public class PaymentsCommandsHandler {
    private final PaymentService paymentService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String paymentsEventsTopicName;

    public PaymentsCommandsHandler(PaymentService paymentService,
                                   KafkaTemplate<String, Object> kafkaTemplate,
                                   @Value("${payments.events.topic.name}") String paymentsEventsTopicName) {
        this.paymentService = paymentService;
        this.kafkaTemplate = kafkaTemplate;
        this.paymentsEventsTopicName = paymentsEventsTopicName;
    }

    @KafkaHandler
    public void handleCommand(@Payload ProcessPaymentCommand command) {
        try {
            System.out.println("Received command: " + command.getOrderId());
            var payment = new Payment(
                    command.getOrderId(),
                    command.getProductId(),
                    command.getProductPrice(),
                    command.getProductQuantity());
            Payment processedPayment = paymentService.process(payment);
            var paymentProcessedEvent = new PaymentProcessedEvent(payment.getOrderId(), processedPayment.getId());
            kafkaTemplate.send(paymentsEventsTopicName, paymentProcessedEvent);
        } catch (CreditCardProcessorUnavailableException e) {
            var paymentFailedEvent = new PaymentFailedEvent(
                    command.getOrderId(),
                    command.getProductId(),
                    command.getProductQuantity());
            kafkaTemplate.send(paymentsEventsTopicName, paymentFailedEvent);
        }
    }
}
