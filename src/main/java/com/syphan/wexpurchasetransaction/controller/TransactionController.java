package com.syphan.wexpurchasetransaction.controller;

import com.syphan.wexpurchasetransaction.util.constant.PathConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = PathConstants.BASE_PATH_TRANSACTION, produces = "application/json")
public class TransactionController {

    @GetMapping
    public String getTransaction() {
        // TODO: implement get transaction
        return "Transaction works";
    }
}
