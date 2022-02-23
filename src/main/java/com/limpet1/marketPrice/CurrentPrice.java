package com.limpet1.marketPrice;

import com.binance.connector.client.impl.SpotClientImpl;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;

import java.util.LinkedHashMap;

@Controller
public class CurrentPrice {

    public Double averagePrice(String currency) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();

        SpotClientImpl client = new SpotClientImpl();

        parameters.put("symbol", currency);

        try {
            String result = client.createMarket().averagePrice(parameters);
            if (result.isEmpty()) {
                return 4.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        double x = 2.0;
        return x;
    }

}
