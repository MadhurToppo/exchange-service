package de.scalable.capital.exchange.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyPair {
    private String name;
    private Float rate;
}
