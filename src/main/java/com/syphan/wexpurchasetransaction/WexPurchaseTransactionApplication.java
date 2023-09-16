package com.syphan.wexpurchasetransaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class WexPurchaseTransactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(WexPurchaseTransactionApplication.class, args);
	}

}
