package com.limpet1.controllers.linedAccount;

import com.limpet1.marketPrice.CurrentPrice;
import com.limpet1.model.XUser;
import com.limpet1.repository.AscendexRepository;
import com.limpet1.repository.BinanceRepository;
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
public class LinkedController {

    private final UserRepositoryJPA userRepositoryJPA;
    public final CurrentPrice currentPrice;
    private final BinanceRepository binanceRepository;
    private final AscendexRepository ascendexRepository;

    public LinkedController(UserRepositoryJPA userRepositoryJPA, AscendexRepository ascendexRepository,
                            AscendexRestControllerV3 ascendexRestControllerV3, BinanceRestControllerV2 binanceRestControllerV2,
                            CurrentPrice currentPrice, BinanceRepository binanceRepository, AscendexRepository ascendexRepository1) {
        this.userRepositoryJPA = userRepositoryJPA;
        this.currentPrice = currentPrice;
        this.binanceRepository = binanceRepository;
        this.ascendexRepository = ascendexRepository1;
    }

    @GetMapping("/linked-accounts")
    public String mainPage(Model model) throws IOException, InterruptedException {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        XUser xUser = userRepositoryJPA.findByEmail(user.getUsername());
        String email = xUser.getEmail();
        List<String> list = Arrays.asList("ACCOUNT NAME", "ACCOUNT EMAIL", "IBAN (API Key)", "BIC/SWIFT", "CURRENCY");
        List<Map<String, Object>> columns = new ArrayList<>();

        var binanceList = binanceRepository.findByUsersId(xUser.getId());
        var ascendexList = ascendexRepository.findByUsersId(xUser.getId());

        for (var i : binanceList) {

            columns.add(Map.of("ACCOUNT NAME", i.getAccount_name(), "ACCOUNT EMAIL", i.getBinance_email(), "IBAN (API Key)",
                    i.getPublic_key(),"BIC/SWIFT", i.getBic_swift(), "CURRENCY", i.getCurrency()));
        }

        for (var i : ascendexList) {
            columns.add(Map.of("ACCOUNT NAME", i.getAccount_name(), "ACCOUNT EMAIL", i.getEmail(), "IBAN (API Key)",
                    i.getApiKey(),"BIC/SWIFT", "empty", "CURRENCY", "EUR"));
        }


        ////

        model.addAttribute("headers", list);
        model.addAttribute("binanceList", binanceList);
        ///
        model.addAttribute("xUser", xUser);
        model.addAttribute("list", list);
        model.addAttribute("columns", columns);

        return "linked-accounts";
    }
}