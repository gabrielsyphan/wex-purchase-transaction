package com.syphan.wexpurchasetransaction.service;

import com.syphan.wexpurchasetransaction.client.DataTreasuryClient;
import com.syphan.wexpurchasetransaction.model.dto.DataTreasuryExchangeDto;
import com.syphan.wexpurchasetransaction.model.dto.DataTreasuryResponseDto;
import com.syphan.wexpurchasetransaction.model.dto.TransactionDto;
import com.syphan.wexpurchasetransaction.model.entity.Transaction;
import com.syphan.wexpurchasetransaction.model.entity.User;
import com.syphan.wexpurchasetransaction.model.mapper.TransactionMapper;
import com.syphan.wexpurchasetransaction.repository.TransactionRepository;
import com.syphan.wexpurchasetransaction.service.transaction.TransactionService;
import com.syphan.wexpurchasetransaction.util.exception.InvalidTransactionException;
import com.syphan.wexpurchasetransaction.util.exception.TransactionNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class TransactionServiceTest {

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private Authentication authentication;

    @MockBean
    private DataTreasuryClient dataTreasuryClient;

    @Autowired
    private TransactionService transactionService;

    private User user;

    @BeforeEach
    public void setup() {
        user = User.builder().id((UUID.randomUUID())).name("Test").email("test@test.com").password("123456").build();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(this.user);
    }

    @Test
    public void createTransactionSuccess() {
        TransactionDto transactionDto = TransactionMapper.INSTANCE.entityToDto(this.buildTransactionEntity());
        Transaction transactionSaved = this.buildTransactionEntity();

        transactionSaved.setId(UUID.randomUUID());
        when(this.transactionRepository.save(Mockito.any())).thenReturn(transactionSaved);

        assertDoesNotThrow(() -> {
            TransactionDto savedTransaction = this.transactionService.create(transactionDto);

            assertNotNull(savedTransaction);
            assertNotNull(savedTransaction.id());
            assertThat(savedTransaction).usingRecursiveComparison()
                    .ignoringFields("id").isEqualTo(transactionDto);
        });
    }

    @Test
    public void createTransactionFailLimitDescriptionCharacters() {
        Transaction transaction = this.buildTransactionEntity();
        transaction.setDescription("012345678901234567890123456789012345678901234567890123456789");

        assertThrows(InvalidTransactionException.class, () -> {
            this.transactionService.create(TransactionMapper.INSTANCE.entityToDto(transaction));
        });
    }

    @Test
    public void createTransactionFailInvalidDate() {
        Transaction transaction = this.buildTransactionEntity();
        transaction.setDate(LocalDate.now().plusDays(1));

        assertThrows(InvalidTransactionException.class, () -> {
            this.transactionService.create(TransactionMapper.INSTANCE.entityToDto(transaction));
        });
    }

    @Test
    public void getTransactionByIdSuccess() {
        UUID id = UUID.randomUUID();
        String country = "Brazil";
        Transaction transaction = this.buildTransactionEntity();
        transaction.setId(id);

        when(this.dataTreasuryClient.getExchangeByCountry(country, LocalDate.now()))
                .thenReturn(this.buildDataTreasuryResponseDto());
        when(this.transactionRepository.findByIdAndUser(id, this.user)).thenReturn(Optional.of(transaction));

        assertDoesNotThrow(() -> {
            TransactionDto transactionResponse = this.transactionService.getById(id, country);
            assertNotNull(transactionResponse);
            assertNotNull(transactionResponse.exchangeRate());
            assertNotNull(transactionResponse.calculatedAmount());
            assertThat(transactionResponse).usingRecursiveComparison()
                    .ignoringFields("id", "exchangeRate", "calculatedAmount")
                    .isEqualTo(TransactionMapper.INSTANCE.entityToDto(transaction));
        });
    }

    @Test
    public void getTransactionByIdFailEmptyData() {
        UUID id = UUID.randomUUID();
        String country = "abcdefghhkalksje";
        Transaction transaction = this.buildTransactionEntity();
        transaction.setId(id);

        when(this.dataTreasuryClient.getExchangeByCountry(country, LocalDate.now()))
                .thenReturn(DataTreasuryResponseDto.builder().data(List.of()).build());
        when(this.transactionRepository.findByIdAndUser(id, this.user)).thenReturn(Optional.of(transaction));

        assertThrows(TransactionNotFoundException.class, () -> {
            this.transactionService.getById(id, country);
        });
    }

    @Test
    public void getAllTransactionByUser() {
        Page<Transaction> transactionPage = this.buildTransactionPage();
        String country = "Brazil";
        when(this.transactionRepository.findAllByUser(this.user, PageRequest.of(0, 50))).thenReturn(transactionPage);
        when(this.dataTreasuryClient.getExchangeByCountry(country, LocalDate.now()))
                .thenReturn(this.buildDataTreasuryResponseDto());

        assertDoesNotThrow(() -> {
            Page<TransactionDto> transactionResponse = this.transactionService.getAll(0, 50, country);
            assertNotNull(transactionResponse);
            assertEquals(transactionResponse.getTotalElements(), transactionPage.getTotalElements());
            assertEquals(transactionResponse.getTotalPages(), transactionPage.getTotalPages());
        });
    }

    public Page<Transaction> buildTransactionPage() {
        List<Transaction> transactionDtos = Stream.of(
                this.buildTransactionEntity(),
                this.buildTransactionEntity(),
                this.buildTransactionEntity()
            ).toList();
        PageRequest pageRequest = PageRequest.of(0, 50);
        return new PageImpl<>(transactionDtos, pageRequest, transactionDtos.size());
    }

    private Transaction buildTransactionEntity() {
        return Transaction.builder()
                .id(null).amount(BigDecimal.valueOf(100))
                .description("Test").date(LocalDate.now()).user(this.user)
                .build();
    }

    private DataTreasuryExchangeDto buildDataTrasuryExchangeDto() {
        return DataTreasuryExchangeDto.builder().exchangeRate("5.00").countryCurrencyDesc("Brazil").currency("BRL").build();
    }

    private DataTreasuryResponseDto buildDataTreasuryResponseDto() {
        return DataTreasuryResponseDto.builder().data(List.of(this.buildDataTrasuryExchangeDto())).build();
    }
}
