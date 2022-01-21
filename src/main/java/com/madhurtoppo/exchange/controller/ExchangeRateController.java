package com.madhurtoppo.exchange.controller;

import com.madhurtoppo.exchange.model.CurrencyPair;
import com.madhurtoppo.exchange.model.Forex;
import com.madhurtoppo.exchange.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    /**
     * Adds all currencies to the database
     * @param currencies list with their names and rates against Euro in Json Format
     * @return List of newly added exchange rates of currencies against Euro
     */
    @PostMapping("/exchange")
    public ResponseEntity<List<Forex>> addAllCurrencies(@RequestBody List<Forex> currencies) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exchangeRateService.addCurrencies(currencies));
    }

    /**
     * This method exposes an Endpoint to get exchange rate of a currency against Euro
     * @param currency whose rate against Euro has to be found
     * @return the Euro Exchange Rate for a particular currency
     */
    @GetMapping("/exchange/{currency}")
    public ResponseEntity<CurrencyPair> getEuroExchangeRate(@PathVariable String currency) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exchangeRateService.getEuroPair(currency));
    }

    /**
     * This method exposed and Endpoint to get exchange rate of 2 non-Euro currencies
     * @param currency1
     * @param currency2
     * @return exchange rate
     */
    @GetMapping("/exchange/{currency1}/{currency2}")
    public ResponseEntity<CurrencyPair> getCurrencyPairRate(@PathVariable String currency1, @PathVariable String currency2) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exchangeRateService.getCurrencyPair(currency1, currency2));
    }

    /**
     * This method gets the list of all foreign currencies and their corresponding rates against Euro
     * @return
     */
    @GetMapping("/exchange")
    public ResponseEntity<List<Forex>> getAllCurrencies() {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exchangeRateService.getCurrencies());
    }

    /**
     * This method exchanges an amount from one currency to another
     * @param currency1
     * @param currency2
     * @param amount
     * @return
     */
    @GetMapping("exchange/{currency1}/{currency2}/{amount}")
    public ResponseEntity<String> convertAmount(@PathVariable String currency1, @PathVariable String currency2, @PathVariable String amount) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exchangeRateService.calculateAmount(currency1, currency2, amount));
    }

    /**
     * This method retrieves a link for a public website for the currency pair
     * @param currency1
     * @param currency2
     * @return
     */
    @GetMapping("/link/{currency1}/{currency2}")
    public ResponseEntity<String> retrieveCurrencyLink(@PathVariable String currency1, @PathVariable String currency2) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(exchangeRateService.getCurrencyPairLink(currency1, currency2));
    }

}
