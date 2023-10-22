package com.example.eshopbackend.eshopbackend.common.utills;

public enum InvoiceStateCode {

    CREATED("CREATED"),
    ORDER_PLACED("ORDER_PLACED"),
    ORDER_SHIPPED("ORDER_SHIPPED"),
    ORDER_OUT_FOR_DELIVERY("ORDER_OUT_FOR_DELIVERY");
    private final String value;

    private InvoiceStateCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String get(String type){
        for(InvoiceStateCode v : values()){
            if( v.name().equals(type) ){
                return v.getValue();
            }
        }
        return null;
    }
}
