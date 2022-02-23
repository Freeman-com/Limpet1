package com.limpet1.rest.binance;

import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Market;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = {"/api/binance/v1"}, produces = APPLICATION_JSON_VALUE)
public class BinanceMarketControllerV1 {

    private static final Logger logger = LoggerFactory.getLogger(BinanceRestControllerV2.class);

    @GetMapping(value = "/exchangeInfo")
    public String exchangeInfo() {

        SpotClientImpl client = new SpotClientImpl();
        Market market = client.createMarket();
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();

        String result = market.exchangeInfo(parameters);
        logger.info(result);

        parameters.put("symbol", "BTCUSDT");
        result = market.exchangeInfo(parameters);
        logger.info(result);
        parameters.clear();

        ArrayList<String> symbols = new ArrayList<>();
        symbols.add("BTCUSDT");
        symbols.add("BNBUSDT");
        parameters.put("symbols", symbols);
        result = market.exchangeInfo(parameters);
        logger.info(result);
        return result;
    }

    /*Average price for 5 min - with parameters currency*/
    @GetMapping(value = "/averagePrice")
    public String averagePrice() {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();

        SpotClientImpl client = new SpotClientImpl();

        parameters.put("symbol", "BTCUSDT");
        String result = client.createMarket().averagePrice(parameters);
        logger.info(result);
        return result;
    }

    @GetMapping(value = "/trades")
    public String trades() {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();

        SpotClientImpl client = new SpotClientImpl();

        parameters.put("symbol", "BTCUSDT");
        String result = client.createMarket().trades(parameters);
        logger.info(result);
        return result;
    }

}
