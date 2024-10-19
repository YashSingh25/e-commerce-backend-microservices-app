package com.yash.ecommerce.order.payment;

import com.yash.ecommerce.order.customer.CustomerResponse;
import com.yash.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(

        BigDecimal amount,

        PaymentMethod paymentMethod,

        Integer orderId,

        String orderReference,

        CustomerResponse customer
) {
}
