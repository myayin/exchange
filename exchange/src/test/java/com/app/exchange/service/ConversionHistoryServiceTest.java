package com.app.exchange.service;

import com.app.exchange.dto.CurrencyConversionHistoryResponse;
import com.app.exchange.model.CurrencyConversionHistory;
import com.app.exchange.repository.CurrencyConversionHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ConversionHistoryServiceTest {
    @Mock
    private CurrencyConversionHistoryRepository repository;

    @InjectMocks
    private ConversionHistoryService service;


    @Test
    void getConversionHistory_shouldReturnResponse_whenTransactionIdExists() {
        CurrencyConversionHistory history = new CurrencyConversionHistory();
        history.setId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(history));

        CurrencyConversionHistoryResponse response = service.getConversionHistory(1L, null, Pageable.unpaged());

        assertNotNull(response);
        assertEquals(1, response.getCurrencyConversionHistoryDtoList().size());
    }

    @Test
    void getConversionHistory_shouldReturnEmptyList_whenTransactionIdNotFound() {
        Mockito.when(repository.findById(2L)).thenReturn(Optional.empty());

        CurrencyConversionHistoryResponse response = service.getConversionHistory(2L, null, Pageable.unpaged());

        assertNotNull(response);
        assertTrue(response.getCurrencyConversionHistoryDtoList().isEmpty());
    }

    @Test
    void getConversionHistory_shouldReturnResponse_whenDateIsProvided() {
        LocalDate date = LocalDate.of(2025, 6, 9);
        Instant start = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        List<CurrencyConversionHistory> historyList = List.of(new CurrencyConversionHistory());
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(repository.findByTransactionDateBetween(start, end, pageable)).thenReturn(historyList);

        CurrencyConversionHistoryResponse response = service.getConversionHistory(null, date, pageable);

        assertNotNull(response);
        assertEquals(1, response.getCurrencyConversionHistoryDtoList().size());
    }

    @Test
    void getConversionHistory_shouldThrowException_whenNoFilterIsProvided() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.getConversionHistory(null, null, Pageable.unpaged())
        );

        assertEquals("At least one filter (transactionId or date) must be provided.", exception.getMessage());
    }
}
