package com.syphan.wexpurchasetransaction.model.mapper;

import com.syphan.wexpurchasetransaction.model.dto.TransactionDto;
import com.syphan.wexpurchasetransaction.model.entity.Transaction;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "user", ignore = true)
    Transaction dtoToEntity(TransactionDto dto);

    TransactionDto entityToDto(Transaction entity);

    @Mapping(target = "id", ignore = true)
    Transaction updateEntity(Transaction transaction, @MappingTarget Transaction transactionUpdate);

    @Mapping(target = "exchangeRate", source = "exchangeRate")
    @Mapping(target = "calculatedAmount", source = "calculatedAmount")
    TransactionDto entityToDto(Transaction transaction, BigDecimal exchangeRate, BigDecimal calculatedAmount);
}
