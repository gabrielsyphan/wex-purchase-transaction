package com.syphan.wexpurchasetransaction;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableWebSecurity
@EnableCaching
public class WexPurchaseTransactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(WexPurchaseTransactionApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public CacheManager cacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager("exchanges");
		cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(2, TimeUnit.HOURS));
		return cacheManager;
	}
}
