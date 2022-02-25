package com.limpet1.marketPrice;

import com.binance.connector.client.impl.SpotClientImpl;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.LinkedHashMap;

@Controller
public class CurrentPrice {

    public Double averagePriceImpl(String currency) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();

        SpotClientImpl client = new SpotClientImpl();
        parameters.put("symbol", currency + "USDT");

//        Double i = null;
//        try {
//            String result = client.createMarket().averagePrice(parameters);
//            if (currency.equals("USDTUSDT") | currency.equals("USDCUSDT") | currency.equals("ASDUSDT")) {
//                i = 1.0;
//            } else {
//
//                Gson gson = new Gson();
//                ModelS modelS = gson.fromJson(result, ModelS.class);
//                String s = modelS.getPrice();
//                i = Double.valueOf(s);
//            }
//
//        } catch (JsonSyntaxException e) {
//            e.printStackTrace();
//
//        }
        return 1.0;
    }
}
