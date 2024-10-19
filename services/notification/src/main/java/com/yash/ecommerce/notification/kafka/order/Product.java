package com.yash.ecommerce.notification.kafka.order;

import java.math.BigDecimal;

public record Product(
        String productId,
        String name,
        String description,
        BigDecimal price,
        double quantity
) {
}
