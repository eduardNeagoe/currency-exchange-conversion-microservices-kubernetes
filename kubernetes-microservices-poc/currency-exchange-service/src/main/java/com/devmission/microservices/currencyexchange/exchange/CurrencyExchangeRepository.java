package com.devmission.microservices.currencyexchange.exchange;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange, Long> {

    Optional<CurrencyExchange> findBySrcCcyAndDestCcy(String srcCcy, String destCcy);
}
