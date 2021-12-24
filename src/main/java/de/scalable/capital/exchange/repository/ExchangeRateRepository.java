package de.scalable.capital.exchange.repository;

import de.scalable.capital.exchange.model.Forex;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<Forex, Integer> {
    Forex findByCurrency(String currency);
}
