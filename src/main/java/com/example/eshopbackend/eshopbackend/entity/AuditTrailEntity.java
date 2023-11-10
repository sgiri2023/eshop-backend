package com.example.eshopbackend.eshopbackend.entity;

import com.example.eshopbackend.eshopbackend.common.enumConstant.InvoiceStateCode;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "audit_trail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditTrailEntity {
    private transient static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(length = 40)
    @Enumerated(value = EnumType.STRING)
    @NotNull
    InvoiceStateCode invoiceState;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    InvoiceEntity invoiceEntity;

    @CreatedDate
    @Column(name = "created_date")
    private Date createdDate;

}
