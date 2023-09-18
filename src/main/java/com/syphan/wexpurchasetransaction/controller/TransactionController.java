package com.syphan.wexpurchasetransaction.controller;

import com.syphan.wexpurchasetransaction.util.exception.InvalidTransactionException;
import com.syphan.wexpurchasetransaction.util.exception.TransactionNotFoundException;
import com.syphan.wexpurchasetransaction.model.dto.TransactionDto;
import com.syphan.wexpurchasetransaction.service.transaction.TransactionService;
import com.syphan.wexpurchasetransaction.util.constant.PathConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(value = PathConstants.BASE_PATH_TRANSACTION, produces = "application/json")
@Tag(name = "Transaction", description = "The transaction API")
@SecurityRequirement(name = "bearerAuth")
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(consumes = "application/json")
    @Operation(summary = "Create new transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created"),
            @ApiResponse(responseCode = "400", description = "Invalid transaction"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    public ResponseEntity<TransactionDto> create(
            @Parameter(description = "Transaction object that needs to be created", required = true)
            @RequestBody @Valid TransactionDto transactionDto,
            UriComponentsBuilder uriComponentsBuilder) throws InvalidTransactionException {
        TransactionDto result = this.transactionService.create(transactionDto);
        URI uri = uriComponentsBuilder.path(PathConstants.BASE_PATH_TRANSACTION + "/{id}").buildAndExpand(result.id()).toUri();
        return ResponseEntity.created(uri).body(result);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get transaction by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found"),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<TransactionDto> getById(
            @Parameter(description = "Transaction id", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
            @PathVariable("id") UUID id,
            @Parameter(description = "Country to exchange currency", example = "Brazil", required = true)
            @RequestParam String country) throws TransactionNotFoundException {
        return ResponseEntity.ok(this.transactionService.getById(id, country));
    }

    @GetMapping
    @Operation(summary = "Get all transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Page<TransactionDto>> getAll(
            @Parameter(description = "Current page (starting at 0)", example = "0", required = true)
            @RequestParam int page,
            @Parameter(description = "Number of items per page", example = "10", required = true)
            @RequestParam int size,
            @Parameter(description = "Country to exchange currency", example = "Brazil", required = true)
            @RequestParam String country) throws TransactionNotFoundException {
        return ResponseEntity.ok(this.transactionService.getAll(page, size, country));
    }
}
