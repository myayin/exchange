package com.app.exchange.service;

import com.app.exchange.dto.CurrencyConversionResponse;
import com.app.exchange.model.CurrencyConversionHistory;
import com.app.exchange.repository.CurrencyConversionHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CsvFileConversionServiceTest {

    @Mock
    private ExchangeClient exchangeClient;

    @Mock
    private CurrencyConversionHistoryRepository repository;

    @InjectMocks
    private CsvFileConversionService csvFileConversionService;



    @Test
    void convert_shouldReturnDtos_whenValidCsvProvided() {
        CurrencyConversionHistory savedHistory = new CurrencyConversionHistory();
        savedHistory.setId(1L);
        savedHistory.setAmount(BigDecimal.valueOf(100));
        savedHistory.setConvertedAmount(BigDecimal.valueOf(85));
        savedHistory.setSourceCurrency("USD");
        savedHistory.setTargetCurrency("EUR");
        when(repository.save(Mockito.any())).thenReturn(savedHistory);

        String csvContent = "sourceCurrency;targetCurrency;amount\nUSD;EUR;100.00";
        MultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        BigDecimal mockRate = new BigDecimal("0.85");
        when(exchangeClient.getExchangeRate("USD", "EUR")).thenReturn(mockRate);

        CurrencyConversionResponse response = csvFileConversionService.convert(file);

        assertNotNull(response);
        assertEquals(1, response.getCurrencyConversionDtoList().size());
        assertEquals(1, response.getCurrencyConversionDtoList().get(0).getTransactionId());
        assertEquals(BigDecimal.valueOf(85).intValue(), response.getCurrencyConversionDtoList().get(0).getConvertedAmount().intValue());
    }

    @Test
    void convert_shouldSkipInvalidLine_whenLineHasWrongFormat() {
        CurrencyConversionHistory savedHistory = new CurrencyConversionHistory();
        savedHistory.setId(1L);
        savedHistory.setAmount(BigDecimal.valueOf(100));
        savedHistory.setConvertedAmount(BigDecimal.valueOf(85));
        savedHistory.setSourceCurrency("USD");
        savedHistory.setTargetCurrency("EUR");
        when(repository.save(Mockito.any())).thenReturn(savedHistory);

        String csvContent = "sourceCurrency;targetCurrency;amount\nUSD;EUR\nUSD;EUR;100.00";

        MultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        BigDecimal mockRate = new BigDecimal("0.85");
        when(exchangeClient.getExchangeRate("USD", "EUR")).thenReturn(mockRate);

        CurrencyConversionResponse response = csvFileConversionService.convert(file);

        assertNotNull(response);
        assertEquals(1, response.getCurrencyConversionDtoList().size());
    }

    @Test
    void supports_shouldReturnTrue_whenInputIsMultipartFile() {
        MultipartFile mockFile = new MockMultipartFile("file", new byte[]{});
        assertTrue(csvFileConversionService.supports(mockFile));
    }

    @Test
    void supports_shouldReturnFalse_whenInputIsNotMultipartFile() {
        assertFalse(csvFileConversionService.supports("not a file"));
    }

}
