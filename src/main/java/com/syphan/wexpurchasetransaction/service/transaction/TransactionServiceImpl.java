package com.syphan.wexpurchasetransaction.service.transaction;

import com.syphan.wexpurchasetransaction.client.DataTreasuryClient;
import com.syphan.wexpurchasetransaction.util.exception.InvalidTransactionException;
import com.syphan.wexpurchasetransaction.util.exception.TransactionNotFoundException;
import com.syphan.wexpurchasetransaction.model.dto.DataTreasuryExchangeDto;
import com.syphan.wexpurchasetransaction.model.dto.DataTreasuryResponseDto;
import com.syphan.wexpurchasetransaction.model.dto.TransactionDto;
import com.syphan.wexpurchasetransaction.model.entity.Transaction;
import com.syphan.wexpurchasetransaction.model.entity.User;
import com.syphan.wexpurchasetransaction.model.mapper.TransactionMapper;
import com.syphan.wexpurchasetransaction.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final Logger logger = Logger.getLogger(TransactionServiceImpl.class.getName());
    private final TransactionRepository transactionRepository;
    private final DataTreasuryClient dataTreasuryClient;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, DataTreasuryClient dataTreasuryClient) {
        this.transactionRepository = transactionRepository;
        this.dataTreasuryClient = dataTreasuryClient;
    }

    @Override
    public TransactionDto create(TransactionDto transactionDto) throws InvalidTransactionException {
        try {
            this.logger.info("TransactionServiceImpl -> create(): " + transactionDto);

            if(transactionDto.description().length() > 50) {
                throw new InvalidTransactionException("Description must be less than 50 characters.");
            }

            if(transactionDto.date().isAfter(LocalDate.now())) {
                throw new InvalidTransactionException("Invalid date. You can't create a transaction that doesn't happen yet.");
            }

            Transaction transaction = TransactionMapper.INSTANCE.dtoToEntity(transactionDto);
            transaction.setAmount(transactionDto.amount().setScale(2, RoundingMode.HALF_UP));
            transaction.setUser(this.getAuthenticatedUser());

            return TransactionMapper.INSTANCE.entityToDto(this.transactionRepository.save(transaction));
        } catch (InvalidTransactionException e) {
            this.logger.severe("TransactionServiceImpl -> create(): Invalid transaction: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            this.logger.severe("TransactionServiceImpl -> create(): Failed to create transaction: " + e.getMessage());
            throw new InvalidTransactionException("Failed to create transaction. "+ e.getMessage());
        }
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @Override
    public TransactionDto getById(UUID id, String country) {
        Transaction transaction = this.transactionRepository.findByIdAndUser(id, this.getAuthenticatedUser()).orElseThrow(
                        () -> new TransactionNotFoundException("Transaction not found."));

        return this.convertTransaction(transaction, country);
    }

    @Override
    public Page<TransactionDto> getAll(int page, int size, String country) {
        return this.transactionRepository
                .findAllByUser(this.getAuthenticatedUser(), PageRequest.of(page, size))
                .map(transaction -> this.convertTransaction(transaction, country));
    }

    private TransactionDto convertTransaction(Transaction transaction, String country) {
        DataTreasuryResponseDto exchangeListByDate = this.dataTreasuryClient.getExchangeByCountry(country, transaction.getDate());
        if(exchangeListByDate.data().isEmpty()) {
            throw new TransactionNotFoundException("Exchange rate not found for the country and transaction date.");
        }

        DataTreasuryExchangeDto updatedExchangeUntilTransactionDate = exchangeListByDate.data().get(exchangeListByDate.data().size() - 1);
        BigDecimal exchangeRate = new BigDecimal(updatedExchangeUntilTransactionDate.exchangeRate()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal calculatedAmount = exchangeRate.multiply(transaction.getAmount()).setScale(2, RoundingMode.HALF_UP);

        return TransactionMapper.INSTANCE.entityToDto(
                transaction,
                exchangeRate,
                calculatedAmount
        );
    }
}
