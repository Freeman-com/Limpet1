package com.limpet1.controllers.test;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class TestController {

    private static final String MAIN = "test/maintest.html";
    private final UserRepositoryJPA userRepositoryJPA;
    private final AscendexRepository ascendexRepository;
    private final BinanceRestControllerV2 binanceRestControllerV2;
    private final AscendexRestControllerV3 ascendexRestControllerV3;
    public final CurrentPrice currentPrice;

    public TestController(UserRepositoryJPA userRepositoryJPA, AscendexRepository ascendexRepository,
                          BinanceRestControllerV2 binanceRestControllerV2,
                          AscendexRestControllerV3 ascendexRestControllerV3, CurrentPrice currentPrice) {
        this.userRepositoryJPA = userRepositoryJPA;
        this.ascendexRepository = ascendexRepository;
        this.binanceRestControllerV2 = binanceRestControllerV2;
        this.ascendexRestControllerV3 = ascendexRestControllerV3;
        this.currentPrice = currentPrice;
    }

    @GetMapping("/maintest")
    public String mainPage(Model model) throws IOException, InterruptedException {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        XUser xUser = userRepositoryJPA.findByEmail(user.getUsername());
        String email = xUser.getEmail();
        List<String> list = Arrays.asList("Ticker", "Market Price", "Quantity", "Total in USD");
        List<Map<String, Object>> columns = new ArrayList<>();

        /* ---  Exchange rates --- */


        var keyList = ascendexRepository.findByUsersId(xUser.getId());
        var a = binanceRestControllerV2.coinInfo1(email);
        var b = ascendexRestControllerV3.accountInfo(email);


        Double value = null;
        for (var i : a.entrySet()) {
            double x = currentPrice.averagePriceImpl(i.getKey());
            double total = i.getValue() * x;

            columns.add(Map.of("Ticker", i.getKey(), "Market Price", i.getValue() * x + " $", "Quantity",

                    i.getValue() + "  " + i.getKey(), "Total in USD", total + " $"));

            value = total++;
        }


        for (var i : b.entrySet()) {
            double x = currentPrice.averagePriceImpl(i.getKey());
            double total = i.getValue() * x;

            columns.add(Map.of("Ticker", i.getKey(), "Market Price", i.getValue() * x + " $", "Quantity",

                    i.getValue() + "  " + i.getKey(), "Total in USD", total + " $"));

            value = 150234.55;
        }

        model.addAttribute("value", value);
        model.addAttribute("xUser", xUser);
        model.addAttribute("list", list);
        model.addAttribute("columns", columns);

        return MAIN;
    }
}
