package com.devmission.microservices.currencyexchange.exchange;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CurrencyExchangeMapper {

    @InheritInverseConfiguration
    CurrencyExchangeDTO map(CurrencyExchange source);

    @Mapping(target = "environment", source = "environment")
    @Mapping(target = "conversionMultiple", source = "conversionMultiple")
    @Mapping(target = "destCcy", source = "destCcy")
    @Mapping(target = "srcCcy", source = "srcCcy")
    CurrencyExchange map(CurrencyExchangeDTO source);
}
