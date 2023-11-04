package com.example.eshopbackend.eshopbackend.datamodel.Dataset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalOrder {
    List<String> monthList;
    List<Integer> invoiceCountList;
    List<Double> totalMonthPriceList;
}
