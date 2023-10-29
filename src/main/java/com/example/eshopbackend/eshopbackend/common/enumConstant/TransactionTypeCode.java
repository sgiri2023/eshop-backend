package com.example.eshopbackend.eshopbackend.common.enumConstant;

public enum TransactionTypeCode {
    WALLET_DEBIT("WALLET_DEBIT"),
    WALLET_CREDIT("WALLET_CREDIT"),
    TOP_UP_DEBIT("TOP_UP_DEBIT"),
    TOP_UP_CREDIT("TOP_UP_CREDIT");

    private final String value;

    private TransactionTypeCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String get(String type){
        for(TransactionTypeCode v : values()){
            if( v.name().equals(type) ){
                return v.getValue();
            }
        }
        return null;
    }
}
