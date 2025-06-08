package com.app.exchange.repository;

import com.app.exchange.model.CurrencyConversionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyConversionHistoryRepository extends JpaRepository<CurrencyConversionHistory, Long> {
}
