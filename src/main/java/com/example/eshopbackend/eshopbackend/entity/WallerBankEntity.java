package com.example.eshopbackend.eshopbackend.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wallet_bank")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WallerBankEntity implements Serializable {

    private transient static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    private String name;

    private Double balance;

    private String walletId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity userEntity;

    @CreatedDate
    @Column(name = "created_date")
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

    @Column(columnDefinition = "boolean default false")
    Boolean isArchive;

    @OneToMany(mappedBy = "fromWalletBankEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<TransactionEntity> fromTransactionEntity;

    @OneToMany(mappedBy = "toWalletBankEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<TransactionEntity> toTransactionEntity;
}
