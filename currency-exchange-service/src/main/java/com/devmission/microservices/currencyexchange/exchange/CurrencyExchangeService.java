package com.devmission.microservices.currencyexchange.exchange;


import com.devmission.microservices.currencyexchange.exchange.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CurrencyExchangeService {

    private final CurrencyExchangeRepository currencyExchangeRepository;
    private final CurrencyExchangeMapper currencyExchangeMapper;
    private final Environment environment;


    public CurrencyExchangeDTO getExchange(String srcCcy, String destCcy) {
        String port = environment.getProperty("local.server.port");
        String host = environment.getProperty("HOSTNAME");
        String version = "v12";

        log.info("Method getExchange called with source {} and destination {}", srcCcy, destCcy);
        return currencyExchangeRepository.findBySrcCcyAndDestCcy(srcCcy, destCcy).map(currencyExchange -> {
                    currencyExchange.setEnvironment(port + " " + version + " " + host);
                    return currencyExchange;
                })
                .map(currencyExchangeMapper::map).orElseThrow(() -> new EntityNotFoundException("Currency exchange not found!"));
    }
}
