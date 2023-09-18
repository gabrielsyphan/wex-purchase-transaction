package com.syphan.wexpurchasetransaction.model.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DataTreasuryResponseDto(List<DataTreasuryExchangeDto> data) {}