package com.example.eshopbackend.eshopbackend.datamodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletBankResponse {
    private Long id;
    private String name;
    private Double balance;
    private String walletId;
    private Date createdDate;
    private Date lastModifiedDate;
    Boolean isArchive;
}
