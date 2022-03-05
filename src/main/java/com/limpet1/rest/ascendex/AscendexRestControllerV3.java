package com.limpet1.rest.ascendex;

import com.binance.connector.client.impl.SpotClientImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.limpet1.model.XUser;
import com.limpet1.repository.AscendexRepository;
import com.limpet1.repository.UserRepositoryJPA;
import com.limpet1.rest.binance.ModelX;
import org.objectweb.asm.tree.analysis.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = {"/api/ascendex/v3"}, produces = APPLICATION_JSON_VALUE)
public class AscendexRestControllerV3 {
    private static final Logger logger = LoggerFactory.getLogger(AscendexRestControllerV3.class);
    private final UserRepositoryJPA userRepositoryJPA;
    private final AscendexRepository ascendexRepository;

    AscendexConnectorTest ascendexConnectorTest = new AscendexConnectorTest();

    public AscendexRestControllerV3(UserRepositoryJPA userRepositoryJPA, AscendexRepository ascendexRepository) {
        this.userRepositoryJPA = userRepositoryJPA;
        this.ascendexRepository = ascendexRepository;
    }

    @GetMapping(value = "/accountInfo/{email}")
    public Map<String, Double> accountInfo(@PathVariable(name = "email") String email) throws IOException, InterruptedException {


        XUser user = userRepositoryJPA.findByEmail(email);
        var keyList = ascendexRepository.findByUsersId(user.getId());
        Map<String, Double> map = new HashMap<>();

        for (var j : keyList) {
            String result = ascendexConnectorTest.senderMethod(j.getApiKey(), j.getSecret(), j.getGroup());

            Gson gson = new Gson();
            ModelY c = gson.fromJson(result, ModelY.class);

            map = c.getData().stream()
                    .filter(data -> {
                        double totalBalance = Double.parseDouble(data.getTotalBalance());
                        boolean balanceCheck = false;
                        if (totalBalance > 0.0000001) {
                            balanceCheck = true;
                        }
                        return balanceCheck;
                    })
                    .collect(
                            Collectors.toMap(
                                    data -> data.getAsset(),
                                    data -> Double.valueOf(data.getTotalBalance())
                            ));
        }
        return map;
    }
}