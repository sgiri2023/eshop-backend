package com.example.eshopbackend.eshopbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements UserDetails, Serializable {

    private transient static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    private String firstName;

    private String lastName;

    private Boolean isCustomer; // true -> Buyer || false -> Seller

    private String email;

    private String password;

    private Boolean accountExpired;

    private Boolean accountLocked;

    private Boolean credentialsExpired;

    private Boolean enable;

    @CreatedDate
    @Column(name = "created_date")
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

    @Column(columnDefinition = "boolean default false")
    Boolean isArchive;

    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<AddressEntity> addresses;

    @OneToMany(mappedBy = "sellerEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<ProductEntity> products;

    @OneToMany(mappedBy = "buyerEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<InvoiceEntity> invoiceBuyerEntity;

    @OneToMany(mappedBy = "sellerEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<InvoiceEntity> invoiceSellerEntity;

    @OneToMany(mappedBy = "userId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JsonManagedReference
    List<PermissionEntity> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<PermissionEntity> permissionsEntities = new ArrayList<>();
        for (PermissionEntity permissionsEntity : permissions) {
            permissionsEntities.add(permissionsEntity);
        }
        return permissionsEntities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }

    @PrePersist
    void onPrePersist() {
        if (isArchive == null) {
            isArchive = Boolean.FALSE;
        }
    }
}
