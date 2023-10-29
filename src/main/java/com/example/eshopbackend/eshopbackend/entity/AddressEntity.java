package com.example.eshopbackend.eshopbackend.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user_address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressEntity implements Serializable {
    private transient static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    private String fullName;

    private String addressLineOne;

    private String addressLineTwo;

    private String state;

    private String city;

    private Long pincode;

    private Boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity userEntity;

    @OneToOne(mappedBy = "addressEntity")
    private InvoiceEntity invoiceEntity;

    @CreatedDate
    @Column(name = "created_date")
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

}
