package com.limpet1.model.conteiners;

public class Exchange {


    private String exchangeName;

    public Exchange() {}

    public Exchange(String exchangeName) {

        this.exchangeName = exchangeName;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }
}
