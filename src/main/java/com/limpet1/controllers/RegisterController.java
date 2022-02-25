package com.limpet1.controllers;

import com.limpet1.model.Status;
import com.limpet1.model.XUser;
import com.limpet1.repository.UserRepositoryJPA;
import org.dom4j.rule.Mode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Random;

@Controller
public class RegisterController {

    private final UserRepositoryJPA userRepositoryJPA;

    public RegisterController(UserRepositoryJPA userRepositoryJPA) {
        this.userRepositoryJPA = userRepositoryJPA;
    }


    @GetMapping("/registration")
    public String registerMethod(Model model) {
        model.addAttribute("userForm", new XUser());

        return "register";
    }

    @PostMapping("/registration")
    public String registerMethod(@ModelAttribute("userForm") XUser userForm, XUser user,
                                 BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        Random random = new Random();
        long x = random.nextLong();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encPass = encoder.encode(user.getPassword());
        user.setName(user.getName());
        user.setPassword(encPass);
        user.setEmail(user.getEmail());
        user.setStatus(Status.ACTIVE);
        user.setRole("USER");
        user.setId(x);

        userRepositoryJPA.save(user);

        return "/login1";
    }
}
