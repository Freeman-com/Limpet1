package com.limpet1.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class CryptoServicesController {

    @GetMapping("cryptoservices")
    public String mainPage(Model model) throws IOException, InterruptedException {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return "cryptoservices";
    }
}
