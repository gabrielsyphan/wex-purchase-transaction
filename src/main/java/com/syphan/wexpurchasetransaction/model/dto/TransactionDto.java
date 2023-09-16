package com.syphan.wexpurchasetransaction.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
public record TransactionDto (

        UUID id,
        BigDecimal exchangeRate,
        BigDecimal calculatedAmount,

        @NotBlank(message = "Description is required.")
        String description,

        @NotNull(message = "Date is required.")
        LocalDate date,

        @NotNull(message = "Amount is required.")
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0.00.")
        BigDecimal amount
) { }
