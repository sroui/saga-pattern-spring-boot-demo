package com.appsdeveloperblog.payments.service.handler;

import com.appsdeveloperblog.core.dto.PaymentCommand;
import com.appsdeveloperblog.core.types.PaymentCommandType;
import com.appsdeveloperblog.payments.service.PaymentService;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${payments.commands.topic.name}")
public class PaymentsCommandsHandler {
    private final PaymentService paymentService;

    public PaymentsCommandsHandler(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaHandler
    public void handleCommand(@Payload PaymentCommand command) {
        if (command.getType().equals(PaymentCommandType.PROCESS_PAYMENT)) {
            paymentService.process(command.getPayment());
        }
    }
}
