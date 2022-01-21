package com.madhurtoppo.exchange.repository;

import com.madhurtoppo.exchange.model.Forex;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<Forex, Integer> {
    Forex findByCurrency(String currency);
}
