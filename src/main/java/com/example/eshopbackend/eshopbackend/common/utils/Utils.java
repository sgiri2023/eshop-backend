package com.example.eshopbackend.eshopbackend.common.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class Utils {
    public String timeBasedId(String startString) {
        String formatedId = "";
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        String datetime = ft.format(dNow);
        formatedId = startString + datetime;
        return formatedId;
    }

    public String generateWalletId(){
        String startString = "ESHOPWALLET";
        return this.timeBasedId(startString);
    }

    public String generateTransactionId(){
        String startString = "ESHOPTRAN";
        return this.timeBasedId(startString);
    }
    public String generateOrderId(){
        String startString = "ESHOPOD";
        return this.timeBasedId(startString);
    }

    public String generateInvoiceNo(){
        String startString = "ESHOPIN";
        return this.timeBasedId(startString);
    }

    public Date addDays(Date date, Long numberOfDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, numberOfDays.intValue());
        return calendar.getTime();
    }

}
