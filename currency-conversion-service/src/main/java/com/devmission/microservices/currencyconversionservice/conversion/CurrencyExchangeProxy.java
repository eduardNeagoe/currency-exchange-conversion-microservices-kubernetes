package com.devmission.microservices.currencyconversionservice.conversion;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// hardcoded url looking for the env variable named CURRENCY_EXCHANGE_SERVICE_HOST
// if CURRENCY_EXCHANGE_SERVICE_HOST exists it will use it; otherwise it will use localhost:8000 => locally it will use localhost:8000
// CURRENCY_EXCHANGE_SERVICE_HOST is automatically created in Kubernetes by using the pattern <SERVICE>_<NAME>_SERVICE_HOST
//@FeignClient(name = "currency-exchange", url = "${CURRENCY_EXCHANGE_SERVICE_HOST:http://localhost}:8000")

// a better approach is to create a custom env variable CURRENCY_EXCHANGE_URI
// because CURRENCY_EXCHANGE_SERVICE_HOST will only be created if the currency-exchange service is up
@FeignClient(name = "currency-exchange", url = "${CURRENCY_EXCHANGE_URI:http://localhost}:8000")
public interface CurrencyExchangeProxy {

    @GetMapping("/exchange/from/{srcCcy}/to/{destCcy}")
    ResponseEntity<CurrencyConversionDTO> getExchange(@PathVariable String srcCcy, @PathVariable String destCcy);

}
