package com.syphan.wexpurchasetransaction.controller;

import com.syphan.wexpurchasetransaction.exception.InvalidTransactionException;
import com.syphan.wexpurchasetransaction.exception.TransactionNotFoundException;
import com.syphan.wexpurchasetransaction.model.dto.TransactionDto;
import com.syphan.wexpurchasetransaction.service.transaction.TransactionService;
import com.syphan.wexpurchasetransaction.util.constant.PathConstants;

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
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<TransactionDto> create(@RequestBody @Valid TransactionDto transactionDto, UriComponentsBuilder uriComponentsBuilder) throws InvalidTransactionException {
        TransactionDto result = this.transactionService.create(transactionDto);
        URI uri = uriComponentsBuilder.path(PathConstants.BASE_PATH_TRANSACTION + "/{id}").buildAndExpand(result.id()).toUri();
        return ResponseEntity.created(uri).body(result);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TransactionDto> getById(@PathVariable("id") UUID id, @RequestParam String country) throws TransactionNotFoundException {
        return ResponseEntity.ok(this.transactionService.getById(id, country));
    }

    @GetMapping
    public ResponseEntity<Page<TransactionDto>> getAll(@RequestParam int page, @RequestParam int size, @RequestParam String country) throws TransactionNotFoundException {

        return ResponseEntity.ok(this.transactionService.getAll(page, size, country));
    }
}
