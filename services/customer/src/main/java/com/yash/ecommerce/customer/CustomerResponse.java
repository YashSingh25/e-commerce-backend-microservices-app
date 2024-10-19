package com.yash.ecommerce.customer;

public record CustomerResponse(
         String id,
         String firstName,
         String lastname,
         String email,
         Address address
) {
}
