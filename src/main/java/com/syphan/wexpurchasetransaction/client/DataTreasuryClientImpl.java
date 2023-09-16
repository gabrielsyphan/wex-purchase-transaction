package com.syphan.wexpurchasetransaction.client;

import com.syphan.wexpurchasetransaction.model.dto.DataTreasuryResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Component
public class DataTreasuryClientImpl implements DataTreasuryClient {

    private final RestTemplate restTemplate;

    @Autowired
    public DataTreasuryClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @Cacheable("exchanges")
    public DataTreasuryResponseDto getExchangeByCountry(String country, LocalDate date) {
        String BASE_URL = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";
        ResponseEntity<DataTreasuryResponseDto> dataTreasury = this.restTemplate.exchange(
                BASE_URL + "?filter=country:eq:" + country + ",record_date:lte:" + date.toString(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
                );

        return dataTreasury.getBody();
    }
}
