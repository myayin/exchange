package com.app.exchange.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table()
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyConversionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_currency", length = 3, nullable = false)
    private String sourceCurrency;

    @Column(name = "target_currency", length = 3, nullable = false)
    private String targetCurrency;

    @Column(name = "amount", precision = 18, scale = 4, nullable = false)
    private BigDecimal amount;

    @Column(name = "converted_amount", precision = 18, scale = 4, nullable = false)
    private BigDecimal convertedAmount;

    @Column(name = "exchange_rate", precision = 18, scale = 8, nullable = false)
    private BigDecimal exchangeRate;

    @Column(name = "transaction_date", nullable = false)
    private Instant transactionDate;

    @PrePersist
    public void prePersist() {
        if (transactionDate == null) {
            transactionDate = Instant.now();
        }
    }
}
