package com.example.eshopbackend.eshopbackend.service.impl;

import com.example.eshopbackend.eshopbackend.datamodel.UserRequest;
import com.example.eshopbackend.eshopbackend.datamodel.UserResponse;
import com.example.eshopbackend.eshopbackend.entity.PermissionEntity;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;
import com.example.eshopbackend.eshopbackend.modelconverter.UserModelConverter;
import com.example.eshopbackend.eshopbackend.repository.PermissionRepository;
import com.example.eshopbackend.eshopbackend.repository.UserRepository;
import com.example.eshopbackend.eshopbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse addUser(UserRequest userRequest) {

        // Create User First
        UserEntity userEntity = UserModelConverter.requestToEntity(userRequest);
        userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        UserEntity savedUserEntity = userRepository.save(userEntity);
        System.out.println("User Created");
        // Create Permission and map to this User
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setPermissionName("ROLE_USER");
        permissionEntity.setDisplayName("User");
        permissionEntity.setUserId(savedUserEntity);
        PermissionEntity savedPermissionEntity = permissionRepository.save(permissionEntity);
        savedPermissionEntity.setUserId(savedUserEntity);
        permissionRepository.save(savedPermissionEntity);

        System.out.println("Permission Mapped");
        return UserModelConverter.entityToResponse(savedUserEntity);
    }

    public List<UserResponse> getUserList(){
        List<UserEntity> userEntityList = userRepository.findAll();
        List<UserResponse> userReponseList = new ArrayList<>();
        if(!userEntityList.isEmpty()){
            for(UserEntity user : userEntityList){
                userReponseList.add(UserModelConverter.entityToResponse(user));
            }
        }
        return userReponseList;
    }

    public UserResponse getUserByUserName(String userName){
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(userName);
        if (optionalUserEntity.isPresent()) {
            return UserModelConverter.entityToResponse(optionalUserEntity.get());
        }
        System.out.println("User Not Found");
        throw new UsernameNotFoundException(userName);
    }
}
