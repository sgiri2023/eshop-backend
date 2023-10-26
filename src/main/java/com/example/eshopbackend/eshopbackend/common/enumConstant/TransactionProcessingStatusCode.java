package com.example.eshopbackend.eshopbackend.common.enumConstant;

public enum TransactionProcessingStatusCode {

    PROCESSING("PROCESSING"),
    COMPLETED("COMPLETED"),
    FAILED("FAILED"),
    TIMEOUT("TIMEOUT");

    private final String value;

    private TransactionProcessingStatusCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String get(String type){
        for(TransactionProcessingStatusCode v : values()){
            if( v.name().equals(type) ){
                return v.getValue();
            }
        }
        return null;
    }
}
