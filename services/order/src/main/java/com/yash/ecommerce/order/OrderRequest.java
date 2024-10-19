package com.yash.ecommerce.order;

import com.yash.ecommerce.order.product.PurchaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        Integer id,

        String reference,

        @Positive(message = "Order amount should be positive.")
        BigDecimal amount,

        @NotNull(message = "Payment method should be precised.")
        PaymentMethod paymentMethod,

        @NotNull(message = "customer ID should be present.")
        @NotEmpty(message = "customer ID should be present.")
        @NotBlank(message = "customer ID should be present.")
        String customerId,

        @NotEmpty(message = "You should have at least one product.")
        List<PurchaseRequest> products
) {
}
