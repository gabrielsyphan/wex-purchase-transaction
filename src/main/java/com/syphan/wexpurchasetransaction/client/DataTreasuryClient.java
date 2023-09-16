package com.syphan.wexpurchasetransaction.client;

import com.syphan.wexpurchasetransaction.model.dto.DataTreasuryResponseDto;

import java.time.LocalDate;

public interface DataTreasuryClient {

    DataTreasuryResponseDto getExchangeByCountry(String country, LocalDate date);
}
