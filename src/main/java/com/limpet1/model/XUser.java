package com.limpet1.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
public class XUser extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "usersemail")
    private String email;
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "date_of_birth")
    private String date_of_birth;

    @Column(name = "address")
    private String address;

    @Column(name = "verification")
    private String verification;

}
