package com.example.eshopbackend.eshopbackend.service;

import com.example.eshopbackend.eshopbackend.datamodel.AddressRequest;
import com.example.eshopbackend.eshopbackend.datamodel.UserRequest;
import com.example.eshopbackend.eshopbackend.datamodel.UserResponse;

import java.util.List;

public interface UserService {
    public UserResponse addUser(UserRequest userRequest);
    public List<UserResponse> getUserList();
    public List<AddressRequest> addUserAddress(Long userId, AddressRequest addressRequest);
    public List<AddressRequest> getUserAddressList(Long userId);
    public List<UserResponse> getAllSellerAndBuyerUserList();
}
