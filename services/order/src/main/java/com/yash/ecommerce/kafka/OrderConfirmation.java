package com.yash.ecommerce.kafka;

import com.yash.ecommerce.order.customer.CustomerResponse;
import com.yash.ecommerce.order.PaymentMethod;
import com.yash.ecommerce.order.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
