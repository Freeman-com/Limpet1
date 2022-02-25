package com.limpet1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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
}
