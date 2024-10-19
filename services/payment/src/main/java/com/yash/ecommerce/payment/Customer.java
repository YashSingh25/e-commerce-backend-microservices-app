package com.yash.ecommerce.payment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record Customer(
        String id,

        @NotNull(message = "Firstname is required.")
        String firstname,

        @NotNull(message = "Lastname is required.")
        String lastname,

        @NotNull(message = "Email is required.")
        @Email(message = "The provided email is not correctly formatted.")
        String email
) {
}
