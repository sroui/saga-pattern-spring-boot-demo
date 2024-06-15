package com.appsdeveloperblog.core.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CreditCardProcessRequest {
    private BigInteger creditCardNumber;
    private BigDecimal paymentAmount;

    public CreditCardProcessRequest() {
    }

    public CreditCardProcessRequest(BigInteger creditCardNumber, BigDecimal paymentAmount) {
        this.creditCardNumber = creditCardNumber;
        this.paymentAmount = paymentAmount;
    }

    public BigInteger getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(BigInteger creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}
