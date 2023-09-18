package com.syphan.wexpurchasetransaction.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Builder
public record DataTreasuryExchangeDto(

        @JsonFormat(pattern = "yyyy-MM-dd")
        @JsonProperty("record_date")
        @NotNull
        LocalDate recordDate,

        @JsonProperty("country")
        @NotBlank
        String country,

        @JsonProperty("currency")
        @NotBlank
        String currency,

        @JsonProperty("country_currency_desc")
        @NotBlank
        String countryCurrencyDesc,

        @JsonProperty("exchange_rate")
        @Pattern(regexp = "^[0-9]+\\.[0-9]{3}$", message = "Exchange rate format is invalid")
        @NotBlank
        String exchangeRate,

        @JsonFormat(pattern = "yyyy-MM-dd")
        @JsonProperty("effective_date")
        @NotNull
        LocalDate effectiveDate,

        @JsonProperty("src_line_nbr")
        @NotBlank
        String srcLineNbr,

        @JsonProperty("record_fiscal_year")
        @NotBlank
        String recordFiscalYear,

        @JsonProperty("record_fiscal_quarter")
        @NotBlank
        String recordFiscalQuarter,

        @JsonProperty("record_calendar_year")
        @NotBlank
        String recordCalendarYear,

        @JsonProperty("record_calendar_quarter")
        @NotBlank
        String recordCalendarQuarter,

        @JsonProperty("record_calendar_month")
        @NotBlank
        String recordCalendarMonth,

        @JsonProperty("record_calendar_day")
        @NotBlank
        String recordCalendarDay
) {}