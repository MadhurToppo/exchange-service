package de.scalable.capital.exchange.service;

import de.scalable.capital.exchange.model.CurrencyPair;
import de.scalable.capital.exchange.model.Forex;
import java.util.List;

public interface ExchangeRateService {
    List<Forex> addCurrencies(List<Forex> currencies);
    CurrencyPair getEuroPair(String currency);
    CurrencyPair getCurrencyPair(String currency1, String currency2);
    List<Forex> getCurrencies();
    String calculateAmount(String currency1, String currency2, String amount);
    String getCurrencyPairLink(String currency1, String currency2);
}
