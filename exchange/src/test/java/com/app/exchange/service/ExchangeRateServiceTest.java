package com.app.exchange.service;

import com.app.exchange.dto.ExchangeRateDto;
import com.app.exchange.exception.ExchangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExchangeRateServiceTest {
    private ExchangeClient exchangeClient;
    private ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        exchangeClient = mock(ExchangeClient.class);
        exchangeRateService = new ExchangeRateService(exchangeClient);
    }

    @Test
    void exchangeRate_shouldReturnExchangeRateDto_whenExchangeRateExists() throws ExchangeException {
        // Arrange
        String from = "USD";
        String to = "EUR";
        BigDecimal mockRate = BigDecimal.valueOf(0.94);
        when(exchangeClient.getExchangeRate(from, to)).thenReturn(mockRate);

        // Act
        ExchangeRateDto result = exchangeRateService.exchangeRate(from, to);

        // Assert
        assertNotNull(result);
        assertEquals(mockRate, result.getRate());
    }

    @Test
    void exchangeRate_shouldThrowExchangeException_whenExchangeRateIsNull() {
        // Arrange
        String from = "USD";
        String to = "EUR";
        when(exchangeClient.getExchangeRate(from, to)).thenReturn(null);

        // Act & Assert
        assertThrows(ExchangeException.class, () -> exchangeRateService.exchangeRate(from, to));
    }
}

