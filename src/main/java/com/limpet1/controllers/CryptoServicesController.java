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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class CryptoServicesController {

    private static final String CRYPTO = "cryptoservices";
    private static final String TRANSACTION = "transaction";
    private final UserRepositoryJPA userRepositoryJPA;
    private final BinanceRestControllerV2 binanceRestControllerV2;
    public final CurrentPrice currentPrice;

    public CryptoServicesController(UserRepositoryJPA userRepositoryJPA, AscendexRepository ascendexRepository,
                                    BinanceRestControllerV2 binanceRestControllerV2,
                                    AscendexRestControllerV3 ascendexRestControllerV3, CurrentPrice currentPrice) {
        this.userRepositoryJPA = userRepositoryJPA;
        this.binanceRestControllerV2 = binanceRestControllerV2;
        this.currentPrice = currentPrice;
    }

    @GetMapping("cryptoservices")
    public String mainPage(Model model) throws IOException, InterruptedException {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        XUser xUser = userRepositoryJPA.findByEmail(user.getUsername());
        List<String> list = Arrays.asList("Name", "Rated", "Type");
        List<Map<String, Object>> columns = new ArrayList<>();
        String email = xUser.getEmail();

        var a = binanceRestControllerV2.coinInfo1(email);

        for (var i : a.entrySet()) {
            double x = currentPrice.averagePriceImpl(i.getKey());
            double total = i.getValue() * x;

            columns.add(Map.of("Name", i.getKey(), "Rated", i.getValue() * x + " $", "Type", i.getValue()));
        }

        Double value = 1250.001;
        model.addAttribute("list", list);
        model.addAttribute("columns", columns);
        model.addAttribute("xUser", xUser);
        model.addAttribute("value", value);

        return CRYPTO;
    }

    @PostMapping("/create-transaction")
    public String createTransaction(Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        XUser xUser = userRepositoryJPA.findByEmail(user.getUsername());


        model.addAttribute("xUser", xUser);
        return TRANSACTION;
    }
}
