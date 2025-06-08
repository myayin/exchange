package com.app.exchange.controller;

import com.app.exchange.dto.ExchangeRateDto;
import com.app.exchange.dto.Result;
import com.app.exchange.exception.ExchangeException;
import com.app.exchange.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/v0/currency", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CurrencyController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping("/exchange-rate")
    public ResponseEntity<ExchangeRateDto> exchangeRate(@RequestParam String from,
                                                        @RequestParam String to) throws ExchangeException {
        ExchangeRateDto exchangeRateDto = exchangeRateService.exchangeRate(from, to);
        exchangeRateDto.setResult(Result.success());
        return ResponseEntity.ok(exchangeRateDto);
    }
}
