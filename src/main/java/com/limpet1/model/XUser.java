package com.limpet1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
@Data
public class XUser implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "usersemail")
    private String email;
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;
    @Column(name = "status")
    private String status;




}
