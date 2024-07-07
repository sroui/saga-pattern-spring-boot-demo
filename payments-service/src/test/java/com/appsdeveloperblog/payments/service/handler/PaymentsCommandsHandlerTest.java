package com.appsdeveloperblog.payments.service.handler;

import com.appsdeveloperblog.core.dto.Payment;
import com.appsdeveloperblog.core.dto.commands.ProcessPaymentCommand;
import com.appsdeveloperblog.core.dto.events.PaymentFailedEvent;
import com.appsdeveloperblog.core.dto.events.PaymentProcessedEvent;
import com.appsdeveloperblog.core.exceptions.CreditCardProcessorUnavailableException;
import com.appsdeveloperblog.payments.service.PaymentService;
import com.appsdeveloperblog.payments.utils.KafkaAwareIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PaymentsCommandsHandlerTest extends KafkaAwareIntegrationTest {
    @MockBean
    private PaymentService paymentService;
    @SpyBean
    private PaymentsCommandsHandler paymentsCommandsHandler;

    @Test
    void givenProcessPaymentCommand_whenPaymentProcessingSucceeded_thenSendPaymentProcessedEvent() {
        when(paymentService.process(any())).thenReturn(mock(Payment.class));

        mockSagaSendsProcessPaymentCommand();

        verify(paymentsCommandsHandler, timeout(15_000).times(1))
                .handleCommand(any(ProcessPaymentCommand.class));
        verify(paymentService, timeout(15_000).times(1)).process(any(Payment.class));
        verify(kafkaTemplate, timeout(15_000).times(1))
                .send(eq(paymentsEventsTopicName), any(PaymentProcessedEvent.class));
    }

    @Test
    void givenProcessPaymentCommand_whenPaymentProcessingFailed_thenSendPaymentFailedEvent() {
        when(paymentService.process(any())).thenThrow(mock(CreditCardProcessorUnavailableException.class));

        mockSagaSendsProcessPaymentCommand();

        verify(paymentsCommandsHandler, timeout(15_000).times(1))
                .handleCommand(any(ProcessPaymentCommand.class));
        verify(paymentService, timeout(15_000).times(1)).process(any(Payment.class));
        verify(kafkaTemplate, timeout(15_000).times(1))
                .send(eq(paymentsEventsTopicName), any(PaymentFailedEvent.class));
    }

    private void mockSagaSendsProcessPaymentCommand() {
        var command = new ProcessPaymentCommand();
        command.setOrderId(UUID.randomUUID());
        command.setProductId(UUID.randomUUID());
        command.setProductPrice(new BigDecimal(100));
        command.setProductQuantity(2);
        kafkaTemplate.send(paymentsCommandsTopicName, command);
    }
}