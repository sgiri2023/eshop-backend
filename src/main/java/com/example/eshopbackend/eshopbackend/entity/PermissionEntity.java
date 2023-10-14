package com.example.eshopbackend.eshopbackend.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class PermissionEntity implements GrantedAuthority, Serializable {

    private transient static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    private String displayName;

    private String permissionName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity userId;

    @Override
    public String getAuthority() {
        return this.permissionName;
    }
}
