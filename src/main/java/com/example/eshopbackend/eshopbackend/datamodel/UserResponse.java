package com.example.eshopbackend.eshopbackend.datamodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean isCustomer;
    private Boolean isArchive;
    List<PermissionResponse> permission;
    private Boolean accountLocked;
    private Date createdDate;
    private Date lastModifyDate;
}
