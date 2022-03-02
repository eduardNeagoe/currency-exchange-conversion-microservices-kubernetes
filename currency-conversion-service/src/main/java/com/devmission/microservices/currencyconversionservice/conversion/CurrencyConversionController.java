package com.devmission.microservices.currencyconversionservice.conversion;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/conversion")
@AllArgsConstructor
@Slf4j
public class CurrencyConversionController {

    private CurrencyExchangeProxy currencyExchangeProxy;

    @GetMapping("/from/{srcCcy}/to/{destCcy}/quantity/{quantity}")
    public ResponseEntity<CurrencyConversionDTO> getConversion(@PathVariable String srcCcy, @PathVariable String destCcy, @PathVariable BigDecimal quantity) {

        log.info("calculateCurrencyConversion called with {} to {} with {}", srcCcy, destCcy, quantity);

        final CurrencyConversionDTO ccyConvBeforeConv = currencyExchangeProxy.getExchange(srcCcy, destCcy).getBody();

        return ResponseEntity.ok(CurrencyConversionDTO.builder()
                .destCcy(destCcy)
                .srcCcy(srcCcy)
                .quantity(quantity.multiply(ccyConvBeforeConv.getConversionMultiple()))
                .environment(ccyConvBeforeConv.getEnvironment())
                .conversionMultiple(ccyConvBeforeConv.getConversionMultiple()).build());
    }

}
