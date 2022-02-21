package com.limpet1.rest;

import com.binance.connector.client.impl.SpotClientImpl;
import com.limpet1.model.XUser;
import com.limpet1.repository.BinanceRepository;
import com.limpet1.repository.UserRepositoryJPA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = {"/main"}, produces = APPLICATION_JSON_VALUE)
public class AdminRestControllerV1 {
    private static final Logger logger = LoggerFactory.getLogger(AdminRestControllerV1.class);
    private final BinanceRepository binanceRepository;
    private final UserRepositoryJPA userRepositoryJPA;

    public AdminRestControllerV1(BinanceRepository binanceRepository, UserRepositoryJPA userRepositoryJPA) {
        this.binanceRepository = binanceRepository;
        this.userRepositoryJPA = userRepositoryJPA;
    }

    @GetMapping(value = "/accountSnapshot/{email}")
    public String accountSnapshot(@PathVariable(name = "email") String email) {

        XUser user = userRepositoryJPA.findByEmail(email);
        var keyList2 = binanceRepository.findByUsersId(user.getId());
        List<String> list = new ArrayList<>();

        /* Account Snapshot - Binance */
        /*Прилетает 5 снапшотов с разницей в сутки 1м JSON-ом*/
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("type", "SPOT");

        for (var s : keyList2) {
            SpotClientImpl client = new SpotClientImpl(s.getPublic_key(), s.getSecret());
            String result = client.createWallet().accountSnapshot(parameters);


            list.add(result);
        }
        return String.valueOf(list);
    }

    @GetMapping(value = "/assetDetail/{email}")
    public String assetDetail(@PathVariable(name = "email") String email) {
        XUser user = userRepositoryJPA.findByEmail(email);
        var keyList2 = binanceRepository.findByUsersId(user.getId());
        List<String> list = new ArrayList<>();
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();

        for (var s : keyList2) {
            SpotClientImpl client = new SpotClientImpl(s.getPublic_key(), s.getSecret());
            String result = client.createWallet().assetDetail(parameters);
            list.add(result);
        }
        return String.valueOf(list);
    }

    @GetMapping(value = "/coinInfo/{email}")
    public String coinInfo(@PathVariable(name = "email") String email) {
        XUser user = userRepositoryJPA.findByEmail(email);
        var keyList2 = binanceRepository.findByUsersId(user.getId());
        List<String> list = new ArrayList<>();
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();

        for (var s : keyList2) {
            SpotClientImpl client = new SpotClientImpl(s.getPublic_key(), s.getSecret());
            String result = client.createWallet().coinInfo(parameters);
            list.add(result);
        }
        return String.valueOf(list);
    }
}
