package com.devmission.microservices.currencyexchange.exchange;


import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/exchange")
public class CurrencyExchangeController {

    private final CurrencyExchangeService currencyExchangeService;
    private Environment environment;

    @GetMapping(value = "/from/{srcCcy}/to/{destCcy}")
    public ResponseEntity<CurrencyExchangeDTO> getExchange(@PathVariable String srcCcy, @PathVariable String destCcy) {
        return ResponseEntity.ok(currencyExchangeService.getExchange(srcCcy, destCcy));
    }
}
