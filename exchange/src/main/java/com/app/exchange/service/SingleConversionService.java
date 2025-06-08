package com.app.exchange.service;

import com.app.exchange.dto.CurrencyConversionDto;
import com.app.exchange.dto.CurrencyConversionRequest;
import com.app.exchange.dto.CurrencyConversionResponse;
import com.app.exchange.exception.ExchangeException;
import com.app.exchange.repository.CurrencyConversionHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class SingleConversionService extends BaseConversionService implements ConversionStrategy {

    public SingleConversionService(ExchangeClient exchangeClient,
                                   CurrencyConversionHistoryRepository repository) {
        super(exchangeClient, repository);
    }

    @Override
    public boolean supports(Object input) {
        return input instanceof CurrencyConversionRequest;
    }


    public CurrencyConversionResponse convert(Object input) throws ExchangeException {
        CurrencyConversionRequest request = (CurrencyConversionRequest) input;
        CurrencyConversionDto currencyConversionDto = getCurrencyConversionDto(request);
        List<CurrencyConversionDto> currencyConversionDtoList = List.of(currencyConversionDto);
        return new CurrencyConversionResponse()
                .setCurrencyConversionDtoList(currencyConversionDtoList);
    }


}
