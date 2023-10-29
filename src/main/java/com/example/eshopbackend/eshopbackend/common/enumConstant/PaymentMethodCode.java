package com.example.eshopbackend.eshopbackend.common.enumConstant;

public enum PaymentMethodCode {
    WALLET("WALLET"),
    COD("CASH_ON_DELIVERY");

    private final String value;

    private PaymentMethodCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String get(String type){
        for(PaymentMethodCode v : values()){
            if( v.name().equals(type) ){
                return v.getValue();
            }
        }
        return null;
    }
}
