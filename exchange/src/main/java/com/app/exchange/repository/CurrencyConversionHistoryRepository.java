package com.app.exchange.repository;

import com.app.exchange.model.CurrencyConversionHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface CurrencyConversionHistoryRepository extends JpaRepository<CurrencyConversionHistory, Long> {
    List<CurrencyConversionHistory> findByTransactionDateBetween(Instant startDate,
                                                               Instant endDate, Pageable pageable);
}
