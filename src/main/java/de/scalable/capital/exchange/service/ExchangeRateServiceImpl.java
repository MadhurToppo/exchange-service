package de.scalable.capital.exchange.service;

import de.scalable.capital.exchange.exception.ResourceNotFoundException;
import de.scalable.capital.exchange.model.CurrencyPair;
import de.scalable.capital.exchange.model.Forex;
import de.scalable.capital.exchange.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    /**
     * This method add a list of currencies with rates and adds it to the database
     * @param currencies
     * @return
     */
    @Override
    public List<Forex> addCurrencies(List<Forex> currencies) {
        exchangeRateRepository.deleteAll();
        return exchangeRateRepository.saveAll(currencies);
    }

    /**
     * Get the currency pair against euro
     * @param currency
     * @return
     */
    @Override
    public CurrencyPair getEuroPair(String currency) {
        Forex forex = exchangeRateRepository.findByCurrency(currency.toUpperCase());
        checkValidForex(currency, forex);
        incrementRequestCount(forex);
        exchangeRateRepository.save(forex);
        return generateEuroPair(forex);
    }

    public void incrementRequestCount(Forex forex) {
        forex.setRequestCount(forex.getRequestCount() + 1);
    }

    public CurrencyPair generateEuroPair(Forex forex) {
        String name = forex.getCurrency().toUpperCase().concat("/EUR");
        float rate = 1 / Float.parseFloat(forex.getRate());
        return new CurrencyPair(name, rate);
    }

    /**
     * Get currency pair with rate
     * @param currency1
     * @param currency2
     * @return
     */
    @Override
    public CurrencyPair getCurrencyPair(String currency1, String currency2) {
        Forex forex1 = exchangeRateRepository.findByCurrency(currency1.toUpperCase());
        Forex forex2 = exchangeRateRepository.findByCurrency(currency2.toUpperCase());
        checkValidForex(currency1, forex1);
        checkValidForex(currency2, forex2);
        incrementRequestCount(forex1);
        incrementRequestCount(forex2);
        exchangeRateRepository.save(forex1);
        exchangeRateRepository.save(forex2);
        return generateCurrencyPair(forex1, forex2);
    }

    public CurrencyPair generateCurrencyPair(Forex forex1, Forex forex2) {
        String name = forex1.getCurrency().toUpperCase().concat("/").concat(forex2.getCurrency());
        float rate = Float.parseFloat(forex2.getRate()) / Float.parseFloat(forex1.getRate());
        return new CurrencyPair(name, rate);
    }

    /**
     * Gets a list of all currencies from the database
     * @return
     */
    @Override
    public List<Forex> getCurrencies() {
        return exchangeRateRepository.findAll();
    }

    /**
     * Calculates the amount converted from one currency to another
     * @param currency1
     * @param currency2
     * @param amount
     * @return
     */
    @Override
    public String calculateAmount(String currency1, String currency2, String amount) {
        Forex forex1 = exchangeRateRepository.findByCurrency(currency1.toUpperCase());
        Forex forex2 = exchangeRateRepository.findByCurrency(currency2.toUpperCase());
        checkValidForex(currency1, forex1);
        checkValidForex(currency2, forex2);
        return getConvertedAmount(forex1, forex2, amount);
    }

    /**
     * Get the generated pair link for currency pair
     * @param currency1
     * @param currency2
     * @return generated url
     */
    @Override
    public String getCurrencyPairLink(String currency1, String currency2) {
        if (currency1.equals(currency2)) {
            throw new ResourceNotFoundException("Invalid pair of exchange currencies");
        }
        Forex forex1 = exchangeRateRepository.findByCurrency(currency1.toUpperCase());
        Forex forex2 = exchangeRateRepository.findByCurrency(currency2.toUpperCase());
        checkValidForex(currency1, forex1);
        checkValidForex(currency2, forex2);
        incrementRequestCount(forex1);
        incrementRequestCount(forex2);
        String uri = "https://www.investing.com/currencies/";
        return uri.concat(currency1.toLowerCase()).concat("-").concat(currency2.toLowerCase());
    }

    private void checkValidForex(String currency, Forex forex) {
        if (forex == null) {
            throw new ResourceNotFoundException("Invalid Currency " + currency);
        }
    }

    private String getConvertedAmount(Forex forex1, Forex forex2, String amount) {
        CurrencyPair currencyPair;
        if (forex1.getCurrency().equalsIgnoreCase("EUR")) {
            currencyPair = getEuroPair(forex2.getCurrency());
        } else if (forex2.getCurrency().equalsIgnoreCase("EUR")) {
            currencyPair = getEuroPair(forex1.getCurrency());
        } else  {
            currencyPair = getCurrencyPair(forex1.getCurrency(), forex2.getCurrency());
        }
        return String.valueOf(currencyPair.getRate() * Float.parseFloat(amount));
    }
}
