package com.limpet1.controllers;

import com.limpet1.marketPrice.CurrentPrice;
import com.limpet1.model.XUser;
import com.limpet1.repository.AscendexRepository;
import com.limpet1.repository.UserRepositoryJPA;
import com.limpet1.rest.ascendex.AscendexRestControllerV3;
import com.limpet1.rest.binance.BinanceRestControllerV2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.*;

@Controller
public class MainController {

    private static final String MAIN = "main";
    private final UserRepositoryJPA userRepositoryJPA;
    private final AscendexRepository ascendexRepository;
    private final BinanceRestControllerV2 binanceRestControllerV2;
    private final AscendexRestControllerV3 ascendexRestControllerV3;
    public final CurrentPrice currentPrice;

    public MainController(UserRepositoryJPA userRepositoryJPA, AscendexRepository ascendexRepository,
                          BinanceRestControllerV2 binanceRestControllerV2, AscendexRestControllerV3 ascendexRestControllerV3,
                          CurrentPrice currentPrice) {
        this.userRepositoryJPA = userRepositoryJPA;
        this.ascendexRepository = ascendexRepository;
        this.binanceRestControllerV2 = binanceRestControllerV2;
        this.ascendexRestControllerV3 = ascendexRestControllerV3;
        this.currentPrice = currentPrice;
    }

    @GetMapping("/main")
    public String mainPage(Model model) throws IOException, InterruptedException {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        XUser xUser = userRepositoryJPA.findByEmail(user.getUsername());
        String email = xUser.getEmail();
        List<String> list = Arrays.asList("Ticker", "Market Price", "Quantity", "NetCost", "Total in USD");
        List<Map<String, Object>> columns = new ArrayList<>();

        /* ---  Exchange rates --- */


        var keyList = ascendexRepository.findByUsersId(xUser.getId());
        var a = binanceRestControllerV2.coinInfo1(email);
        var b = ascendexRestControllerV3.accountInfo(email);


        for (var i : a.entrySet()) {

            try {
                double x = currentPrice.averagePrice(i.getKey());


                columns.add(Map.of("Ticker", i.getKey(), "Market Price", "$ 0.000", "Quantity",
                        i.getValue() + "  " + i.getKey(), "NetCost", "0" + " $", "Total in USD",
                        i.getValue() * x + " $"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (var i : b.entrySet()) {
            double x = currentPrice.averagePrice(i.getKey());
            columns.add(Map.of("Ticker", i.getKey(), "Market Price", "$ 0.000", "Quantity",
                    i.getValue() + "  " + i.getKey(), "NetCost", "0" + " $", "Total in USD",
                    i.getValue() * x + " $"));
        }
        model.addAttribute("xUser", xUser);
        model.addAttribute("list", list);
        model.addAttribute("columns", columns);

        return MAIN;
    }
}