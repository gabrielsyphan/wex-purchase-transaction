package com.syphan.wexpurchasetransaction.service.transaction;

import com.syphan.wexpurchasetransaction.exception.InvalidTransactionException;
import com.syphan.wexpurchasetransaction.exception.TransactionNotFoundException;
import com.syphan.wexpurchasetransaction.model.dto.TransactionDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface TransactionService {

    TransactionDto create(TransactionDto transactionDto) throws InvalidTransactionException;

    TransactionDto getById(UUID id) throws TransactionNotFoundException;

    Page<TransactionDto> getAll(int page, int size);
}
