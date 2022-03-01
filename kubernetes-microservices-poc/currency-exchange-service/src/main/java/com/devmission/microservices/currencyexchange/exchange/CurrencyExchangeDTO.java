package com.devmission.microservices.currencyexchange.exchange;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyExchangeDTO {

    private String srcCcy;
    private String destCcy;
    private BigDecimal conversionMultiple;
    private String environment;
}
