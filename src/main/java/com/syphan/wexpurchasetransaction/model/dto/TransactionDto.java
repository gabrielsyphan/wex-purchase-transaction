package com.syphan.wexpurchasetransaction.model.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionDto (

        UUID id,

        @NotBlank(message = "Description is required.")
        String description,

        @NotNull(message = "Date is required.")
        LocalDate date,

        @NotNull(message = "Amount is required.")
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0.00.")
        BigDecimal amount,

        UserDto user
) { }
