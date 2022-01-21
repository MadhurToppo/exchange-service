package com.madhurtoppo.exchange.service;

import com.madhurtoppo.exchange.model.Forex;
import com.madhurtoppo.exchange.model.CurrencyPair;

import java.util.List;

public interface ExchangeRateService {
    List<Forex> addCurrencies(List<Forex> currencies);
    CurrencyPair getEuroPair(String currency);
    CurrencyPair getCurrencyPair(String currency1, String currency2);
    List<Forex> getCurrencies();
    String calculateAmount(String currency1, String currency2, String amount);
    String getCurrencyPairLink(String currency1, String currency2);
}
