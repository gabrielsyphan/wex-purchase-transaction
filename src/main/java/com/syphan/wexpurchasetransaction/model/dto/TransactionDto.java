package com.syphan.wexpurchasetransaction.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
public record TransactionDto (

        UUID id,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        BigDecimal exchangeRate,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        BigDecimal calculatedAmount,

        @NotBlank(message = "Description is required.")
        String description,

        @NotNull(message = "Date is required.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @NotNull(message = "Amount is required.")
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0.00.")
        BigDecimal amount
) { }
