package de.scalable.capital.exchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.scalable.capital.exchange.model.CurrencyPair;
import de.scalable.capital.exchange.model.Forex;
import de.scalable.capital.exchange.service.ExchangeRateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class ExchangeRateControllerTest {

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /api/v1/exchange")
    void addAllCurrencies() throws Exception {
        Forex forex1 = new Forex(1, "USD", "1.1336", 0);
        Forex forex2 = new Forex(2, "JPY", "129.37", 0);
        List<Forex> forexList = new ArrayList<>(Arrays.asList(forex1, forex2));

        Mockito.when(exchangeRateService.addCurrencies(forexList)).thenReturn(forexList);
        mockMvc.perform(post("/api/v1/exchange").content(asJsonString(forexList))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/v1/exchange/{currency}")
    void testGetEuroExchangeRate() throws Exception {
        CurrencyPair currencyPair = new CurrencyPair("USD/EUR", (float) 0.8821454);

        Mockito.when(exchangeRateService.getEuroPair("USD")).thenReturn(currencyPair);
        mockMvc.perform(get("/api/v1/exchange/{currency}", "USD"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/v1/exchange/{currency1}/{currency2}")
    void testGetCurrencyExchangeRate() throws Exception {
        CurrencyPair currencyPair = new CurrencyPair("USD/INR", (float) 76.2178);

        Mockito.when(exchangeRateService.getCurrencyPair("USD", "INR")).thenReturn(currencyPair);
        mockMvc.perform(get("/api/v1/exchange/{currency1}/{currency2}", "USD", "INR"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/v1/exchange/{currency1}/{currency2}")
    void testGetAllCurrencies() throws Exception {
        Forex forex1 = new Forex(1, "USD", "1.1336", 0);
        Forex forex2 = new Forex(2, "JPY", "129.37", 0);
        List<Forex> forexList = new ArrayList<>(Arrays.asList(forex1, forex2));

        Mockito.when(exchangeRateService.getCurrencies()).thenReturn(forexList);
        mockMvc.perform(get("/api/v1/exchange"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/v1/exchange/{currency1}/{currency2}/{amount}")
    void testConvertAmount() throws Exception {
        CurrencyPair currencyPair = new CurrencyPair("USD/INR", (float) 76.2178);

        Mockito.when(exchangeRateService.calculateAmount("USD", "INR", "14")).thenReturn("1067.0492");
        mockMvc.perform(get("/api/v1/exchange/{currency1}/{currency2}/{amount}", "USD", "INR", 14))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/v1/exchange/{currency1}/{currency2}/{amount}")
    void testGetCurrencyPairLink() throws Exception {
        CurrencyPair currencyPair = new CurrencyPair("USD/INR", (float) 76.2178);

        Mockito.when(exchangeRateService.getCurrencyPairLink("USD", "INR")).thenReturn("https://www.investing.com/currencies/usd-inr");
        mockMvc.perform(get("/api/v1/link/{currency1}/{currency2}", "USD", "INR"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN));
    }

    static String asJsonString(final List<Forex> forexList) {
        try {
            return new ObjectMapper().writeValueAsString(forexList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}