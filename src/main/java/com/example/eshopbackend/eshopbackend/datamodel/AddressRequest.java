package com.example.eshopbackend.eshopbackend.datamodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    public Long id;
    public String fullName;
    public String addressLineOne;
    public String addressLineTwo;
    public String state;
    public String city;
    public Long pincode;
    public Boolean isDefault;
    private Date createdDate;
    private Date lastModifyDate;
}
