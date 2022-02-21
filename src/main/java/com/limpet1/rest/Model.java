package com.limpet1.rest;

import lombok.Data;

import java.util.List;

@Data
public class Model {
    private String asset;
    private List<String> snapshotVos;
    private Integer code;
    private String data;

}
