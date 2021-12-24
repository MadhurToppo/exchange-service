package de.scalable.capital.exchange.service;

import de.scalable.capital.exchange.model.CurrencyPair;
import de.scalable.capital.exchange.model.Forex;
import de.scalable.capital.exchange.repository.ExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class ExchangeRateServiceImplTest {

    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;

    @MockBean
    private ExchangeRateRepository exchangeRateRepository;

    @Test
    void testGenerateUsdEuroPair() {
        Forex usd = new Forex(1, "USD", "1.1336", 0);
        CurrencyPair usdEuro = exchangeRateService.generateEuroPair(usd);
        assertEquals("USD/EUR", usdEuro.getName());
        assertEquals((float) 0.8821454, usdEuro.getRate().floatValue());
    }

    @Test
    void testGenerateJpyEuroPair() {
        Forex jpy = new Forex(2, "JPY", "129.37", 0);
        CurrencyPair jpyEuro = exchangeRateService.generateEuroPair(jpy);
        assertEquals("JPY/EUR", jpyEuro.getName());
        assertEquals((float) 0.007729768, jpyEuro.getRate().floatValue());
    }

    @Test
    void testGenerateUsdJpyPair() {
        Forex usd = new Forex(1, "USD", "1.1336", 0);
        Forex jpy = new Forex(2, "JPY", "129.37", 0);
        CurrencyPair usdJpy = exchangeRateService.generateCurrencyPair(usd, jpy);
        assertEquals("USD/JPY", usdJpy.getName());
        assertEquals((float) 114.123146, usdJpy.getRate().floatValue());
    }

    @Test
    void testGenerateUsdInrPair() {
        Forex usd = new Forex(1, "USD", "1.1336", 0);
        Forex jpy = new Forex(2, "INR", "86.4005", 0);
        CurrencyPair usdJpy = exchangeRateService.generateCurrencyPair(usd, jpy);
        assertEquals("USD/INR", usdJpy.getName());
        assertEquals((float) 76.2178, usdJpy.getRate().floatValue());
    }

    @Test
    void testIncrementCount() {
        Forex usd = new Forex(1, "USD", "1.1336", 0);
        exchangeRateService.incrementRequestCount(usd);
        assertEquals(1, usd.getRequestCount());
        exchangeRateService.incrementRequestCount(usd);
        assertEquals(2, usd.getRequestCount());
    }

    @Test
    void testCheckValidForex() {
        Forex usd = new Forex(1, "USD", "1.1336", 0);
        Forex jpy = new Forex(2, "INR", "86.4005", 0);

        List<Forex> forexList = new ArrayList<>(Arrays.asList(usd, jpy));
        Mockito.when(exchangeRateRepository.findAll()).thenReturn(forexList);

        assertEquals(forexList, exchangeRateService.getCurrencies());
    }

    @Test
    void testGetEuroPair() {
        Forex usd = new Forex(1, "USD", "1.1336", 0);
        Mockito.when(exchangeRateRepository.findByCurrency("USD")).thenReturn(usd);
        CurrencyPair usdEur = exchangeRateService.getEuroPair("USD");
        assertEquals("USD/EUR", usdEur.getName());
        assertEquals((float) 0.8821454, usdEur.getRate());
    }

    @Test
    void testGetCurrencyPair() {
        Forex usd = new Forex(1, "USD", "1.1336", 0);
        Forex inr = new Forex(2, "INR", "86.4005", 0);
        Mockito.when(exchangeRateRepository.findByCurrency("USD")).thenReturn(usd);
        Mockito.when(exchangeRateRepository.findByCurrency("INR")).thenReturn(inr);
        CurrencyPair usdInr = exchangeRateService.getCurrencyPair("USD", "INR");
        assertEquals("USD/INR", usdInr.getName());
        assertEquals((float) 76.2178, usdInr.getRate());
    }

    @Test
    void testExchangeRateAmountConversion() {
        Forex usd = new Forex(1, "USD", "1.1336", 0);
        Forex inr = new Forex(2, "INR", "86.4005", 0);
        Mockito.when(exchangeRateRepository.findByCurrency("USD")).thenReturn(usd);
        Mockito.when(exchangeRateRepository.findByCurrency("INR")).thenReturn(inr);
        String amount = exchangeRateService.calculateAmount("USD", "INR", "14");
        assertEquals("1067.0492", amount);
    }

    @Test
    void testAddAllCurrencies() {
        Forex usd = new Forex(1, "USD", "1.1336", 0);
        Forex inr = new Forex(2, "INR", "86.4005", 0);
        List<Forex> forexList = new ArrayList<>(Arrays.asList(usd, inr));
        Mockito.when(exchangeRateRepository.saveAll(forexList)).thenReturn(forexList);
        assertEquals(forexList, exchangeRateService.addCurrencies(forexList));
    }

    @Test
    void testRetrieveLink() {
        Forex usd = new Forex(1, "USD", "1.1336", 0);
        Forex inr = new Forex(2, "INR", "86.4005", 0);
        Mockito.when(exchangeRateRepository.findByCurrency("USD")).thenReturn(usd);
        Mockito.when(exchangeRateRepository.findByCurrency("INR")).thenReturn(inr);
        String uri = "https://www.investing.com/currencies/usd-inr";
        assertEquals(uri, exchangeRateService.getCurrencyPairLink("USD", "INR"));
    }
}