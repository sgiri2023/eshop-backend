package com.example.eshopbackend.eshopbackend.service.impl;

import com.example.eshopbackend.eshopbackend.entity.UserEntity;
import com.example.eshopbackend.eshopbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String userName = null;
        String password = null;
        List<GrantedAuthority> authorities = new ArrayList<>();

        System.out.println("Load by User Name");
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(username);
        if (optionalUserEntity.isPresent()) {
            System.out.println("Login User Id: " + optionalUserEntity.get().getId());
            System.out.println("User Details Present: " + optionalUserEntity.get().getEmail());
            // System.out.println("User Details Present: " + optionalUserEntity.get().getPassword());
            // System.out.println("User Permissions" + optionalUserEntity.get().getPermissions());
            return optionalUserEntity.get();
        }
        System.out.println("User Not Found");
        throw new UsernameNotFoundException(username);
    }
}
