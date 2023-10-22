package com.example.eshopbackend.eshopbackend.datamodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditResponse {
    public Long id;
    public String invoiceState;
}
