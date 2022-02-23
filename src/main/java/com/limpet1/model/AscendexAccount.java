package com.limpet1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ascendex_account")
@Data
public class AscendexAccount implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "apikey")
    private String apiKey;

    @Column(name = "secret")
    private String secret;

    @Column(name = "user_id")
    private long usersId;

    @Column(name = "group")
    public int group;

    @Column(name = "email")
    public String email;
}
