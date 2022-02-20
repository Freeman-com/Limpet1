package com.limpet1.controllers;

import com.limpet1.model.XUser;
import com.limpet1.repository.BinanceRepository;
import com.limpet1.repository.UserRepositoryJPA;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class MyassetController {

    private static final String MYASSET_BASE = "myasset";
    private final UserRepositoryJPA userRepositoryJPA;
    private final BinanceRepository binanceRepository;


    public MyassetController(UserRepositoryJPA userRepositoryJPA, BinanceRepository binanceRepository) {
        this.userRepositoryJPA = userRepositoryJPA;
        this.binanceRepository = binanceRepository;
    }

    @GetMapping("/myasset")
    public String mainPage(Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        XUser xUser = userRepositoryJPA.findByEmail(user.getUsername());


        List<String> list = Arrays.asList("Ticker", "Market Price", "Quantity", "NetCost", "Total");
        List<Map<String, Object>> columns = new ArrayList<>();


        var binanceList = binanceRepository.findByUsersId(xUser.getId());

        for (var i : binanceList) {

            columns.add(Map.of("Ticker", i.getBinance_email(), "Market Price", i.getBinance_email(), "Quantity",
                    i.getBinance_email(), "NetCost", i.getBinance_email(), "Total", i.getBinance_email()));
        }


        model.addAttribute("xUser", xUser);
        model.addAttribute("list", list);
        model.addAttribute("columns", columns);

        return MYASSET_BASE;
    }
}