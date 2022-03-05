package com.limpet1.model;

import javax.persistence.*;
import lombok.Data;


import java.io.Serializable;

@Entity
@Table(name = "binance_account")
@Data
public class BinanceAccount implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "users_id")
    private long usersId;

    @Column(name = "public_key")
    private String public_key;

    @Column(name = "secret")
    private String secret;

    @Column(name = "binance_email")
    private String binance_email;

    @Column (name = "exchange_email")
    private String exchange_email;

    @Column (name = "account_name")
    private String account_name;

    @Column (name = "iban_id")
    private String iban_id;


    @Column (name = "currency")
    private String currency;

    @Column (name = "bic_swift")
    private String bic_swift;


}
