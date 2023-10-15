package com.example.eshopbackend.eshopbackend.modelconverter;

import com.example.eshopbackend.eshopbackend.datamodel.PermissionResponse;
import com.example.eshopbackend.eshopbackend.datamodel.UserRequest;
import com.example.eshopbackend.eshopbackend.datamodel.UserResponse;
import com.example.eshopbackend.eshopbackend.entity.PermissionEntity;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserModelConverter {

    public static UserEntity requestToEntity(UserRequest userRequest){

        UserEntity userEntity = new UserEntity();

        if (userRequest.getId() != null) {
            userEntity.setId(userRequest.getId());
        }

        userEntity.setEmail(userRequest.getEmail());
        userEntity.setFirstName(userRequest.getFirstName());
        userEntity.setLastName(userRequest.getLastName());
        userEntity.setIsCustomer(userRequest.getIsCustomer());
        userEntity.setIsArchive(userRequest.getIsArchive());
        userEntity.setEnable(true);
        userEntity.setAccountLocked(false);
        userEntity.setAccountExpired(false);
        userEntity.setCredentialsExpired(false);
        userEntity.setCreatedDate(new Date());
        userEntity.setLastModifiedDate(new Date());
        return userEntity;
    }

    public static UserResponse entityToResponse(UserEntity userEntity){
        UserResponse userResponse = new UserResponse();
        System.out.println("User Entity: " + userEntity);
        if(userEntity.getId() != null){
            userResponse.setId(userEntity.getId());
        }
        userResponse.setEmail(userEntity.getEmail());
        userResponse.setFirstName(userEntity.getFirstName());
        userResponse.setLastName(userEntity.getLastName());
        userResponse.setIsCustomer(userEntity.getIsCustomer());
        userResponse.setIsArchive(userEntity.getIsArchive());
        userResponse.setAccountLocked(userEntity.getAccountLocked());
        userResponse.setCreatedDate(userEntity.getCreatedDate());
        userResponse.setLastModifyDate(userEntity.getLastModifiedDate());
        // System.out.println("Setting Permission Mapping"+ userEntity.getPermissions());
        if(userEntity.getPermissions() != null){
            List<PermissionResponse> permissionResponseArray = new ArrayList<>();
            for(PermissionEntity permission: userEntity.getPermissions()){
                PermissionResponse tempPerResponse = new PermissionResponse();
                tempPerResponse.setId(permission.getId());
                tempPerResponse.setPermission(permission.getPermissionName());
                tempPerResponse.setDisplayName(permission.getDisplayName());
                permissionResponseArray.add(tempPerResponse);
            }
            userResponse.setPermission(permissionResponseArray);
        }

        return userResponse;
    }
}
