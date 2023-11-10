package com.example.eshopbackend.eshopbackend.common.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

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

    public Date getFirstDateOfMonth(Integer monthId, Integer year){
        Calendar gc = new GregorianCalendar();
        gc.set(Calendar.YEAR, year);
        gc.set(Calendar.MONTH, monthId);
        gc.set(Calendar.DAY_OF_MONTH, 1);
        Date date = gc.getTime();
        return date;
    }

    public Date getEndDateOfMonth(Integer monthId, Integer year){
        Calendar gc = new GregorianCalendar();
        gc.set(Calendar.YEAR, year);
        gc.set(Calendar.MONTH, monthId);
        gc.set(Calendar.DAY_OF_MONTH, 1);
        gc.add(Calendar.MONTH, 1);
        gc.add(Calendar.DAY_OF_MONTH, -1);
        Date date = gc.getTime();
        return date;
    }

    public String getMonthNameFromMonthIndex(Integer monthIndex){
        List<String> monthList = new ArrayList<>();
        monthList.add("Jan");
        monthList.add("Feb");
        monthList.add("Mar");
        monthList.add("Apr");
        monthList.add("May");
        monthList.add("Jun");
        monthList.add("July");
        monthList.add("Aug");
        monthList.add("Sep");
        monthList.add("Oct");
        monthList.add("Nov");
        monthList.add("Dec");

        return monthList.get(monthIndex);
    }

    public Integer getMonthNameFromMonthIndex(String monthName){
        List<String> monthList = new ArrayList<>();
        monthList.add("Jan");
        monthList.add("Feb");
        monthList.add("Mar");
        monthList.add("Apr");
        monthList.add("May");
        monthList.add("Jun");
        monthList.add("July");
        monthList.add("Aug");
        monthList.add("Sep");
        monthList.add("Oct");
        monthList.add("Nov");
        monthList.add("Dec");

        return monthList.indexOf(monthName);
    }

    public List<String> getMonthListFromToCurrentDate(Date fromDate){

        List<String> finalMonthList = new ArrayList<>();

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(fromDate);
        int fromYear = calendar.get(Calendar.YEAR);
        int fromMonth = calendar.get(Calendar.MONTH);

        Date date = new Date();
        calendar.setTime(date);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);

        Integer startMonthIndex = fromMonth;
        Integer initialCurrentMonthIndex = fromYear == currentYear ? currentMonth : 12;
        for (int i = fromYear; i <= currentYear; i++) {
            initialCurrentMonthIndex = i == currentYear ? currentMonth + 1 : 12;
            for (int monthIndex = startMonthIndex; monthIndex < initialCurrentMonthIndex; monthIndex++) {
                String monthLabel = this.getMonthNameFromMonthIndex(monthIndex) + "-" + i;
                finalMonthList.add(monthLabel);
            }
            startMonthIndex = 0;
        }
        return finalMonthList;
    }



}
