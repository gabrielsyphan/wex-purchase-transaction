package com.syphan.wexpurchasetransaction.model.mapper;

import com.syphan.wexpurchasetransaction.model.dto.TransactionDto;
import com.syphan.wexpurchasetransaction.model.entity.Transaction;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    Transaction dtoToEntity(TransactionDto dto);

    @Mapping(target = "user.password", ignore = true)
    TransactionDto entityToDto(Transaction entity);

    @Mapping(target = "id", ignore = true)
    Transaction updateEntity(Transaction transaction, @MappingTarget Transaction transactionUpdate);
}
