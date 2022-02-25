package com.limpet1.rest.binance;

import com.binance.connector.client.impl.SpotClientImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.limpet1.model.XUser;
import com.limpet1.repository.BinanceRepository;
import com.limpet1.repository.UserRepositoryJPA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = {"/api/binance/v2"}, produces = APPLICATION_JSON_VALUE)
public class BinanceRestControllerV2 {
    private static final Logger logger = LoggerFactory.getLogger(BinanceRestControllerV2.class);
    private final BinanceRepository binanceRepository;
    private final UserRepositoryJPA userRepositoryJPA;

    public BinanceRestControllerV2(BinanceRepository binanceRepository, UserRepositoryJPA userRepositoryJPA) {
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
    public Map<String, Double> coinInfo1(@PathVariable(name = "email") String email) throws IOException {
        XUser user = userRepositoryJPA.findByEmail(email);
        var keyList2 = binanceRepository.findByUsersId(user.getId());
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        Map<String, Double> map = new HashMap<>();

        for (var s : keyList2) {
            SpotClientImpl client = new SpotClientImpl(s.getPublic_key(), s.getSecret());
            String result = client.createWallet().coinInfo(parameters);

            Gson gson = new Gson();
            Type type = new TypeToken<List<ModelX>>() {
            }.getType();
            List<ModelX> c = gson.fromJson(result, type);

            c.stream() /*Java8 - замена forEach*/
                    .filter(modelX -> {
                        String coin = modelX.getCoin();
                        double free = modelX.getFree();
                        if (free > 0.001 && !Objects.equals(coin, "0.001")) {
                            return true;
                        } else {
                            return false;
                        }
                    })
                    /*терминальная операция - последняя*/
                    .forEach(modelX -> {
                        String coin = modelX.getCoin();
                        double free = modelX.getFree();
                        if (!map.containsKey(coin)) {
                            map.put(coin, free);
                        } else {
                            Double aDouble = map.get(coin);
                            map.put(coin, aDouble + free);
                        }
                    });
//            for (var x : c) {
//                var i = x.getFree();
//                var j = x.getCoin();
//                if (i != 0 & !Objects.equals(j, "0")) {
//                    str.add(x.getCoin());
//                    str.add(x.getFree());
//                } else if (i == 0 && Objects.equals(j, "0")) {
//                    continue;
//                }
//            }
//            list.add(String.valueOf(str));
//            FileWriter writer = new FileWriter("/home/limpet/IdeaProjects/Limpet1/src/main/resources/buffer/binance.json");
//            writer.write(String.valueOf(list));
//            writer.flush();
        }
        return map;
    }
}
