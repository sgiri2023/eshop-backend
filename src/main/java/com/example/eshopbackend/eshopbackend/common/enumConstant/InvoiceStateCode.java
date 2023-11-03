package com.example.eshopbackend.eshopbackend.common.enumConstant;

public enum InvoiceStateCode {
    CREATED("CREATED"),
    ORDER_PLACED("ORDER_PLACED"),
    ORDER_PROCESSING("ORDER_PROCESSING"),
    ORDER_NOT_PROCESSED("ORDER_NOT_PROCESSDED"),
    ORDER_SHIPPED("ORDER_SHIPPED"),
    ORDER_REACHED_NEAREST_HUB("ORDER_REACHED_NEAREST_HUB"),
    ORDER_OUT_FOR_DELIVERY("ORDER_OUT_FOR_DELIVERY"),
    ORDER_DELIVERED("ORDER_DELIVERED"),
    ORDER_NOT_DELIVERED("ORDER_NOT_DELIVERED"),
    ORDER_CANCELLED("ORDER_CANCELLED"),
    ORDER_DECLINED("ORDER_DECLINED"),
    ORDER_SETTLED("ORDER_SETTLED");

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
