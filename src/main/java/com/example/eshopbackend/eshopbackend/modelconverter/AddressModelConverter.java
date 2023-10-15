package com.example.eshopbackend.eshopbackend.modelconverter;

import com.example.eshopbackend.eshopbackend.datamodel.AddressRequest;
import com.example.eshopbackend.eshopbackend.datamodel.UserRequest;
import com.example.eshopbackend.eshopbackend.entity.AddressEntity;
import com.example.eshopbackend.eshopbackend.entity.UserEntity;

import java.util.Date;

public class AddressModelConverter {

    public static AddressEntity requestToEntity(AddressRequest addressRequest, UserEntity userEntity){

        AddressEntity addressEntity = new AddressEntity();

        if (addressRequest.getId() != null) {
            addressEntity.setId(addressRequest.getId());
        }
        if (userEntity != null) {
            addressEntity.setUserId(userEntity);
        }

        addressEntity.setFullName(addressRequest.getFullName());
        addressEntity.setAddressLineOne(addressRequest.getAddressLineOne());
        addressEntity.setAddressLineTwo(addressRequest.addressLineTwo);
        addressEntity.setState(addressRequest.getState());
        addressEntity.setCity(addressRequest.getCity());
        addressEntity.setPincode(addressRequest.getPincode());
        addressEntity.setIsDefault(addressRequest.isDefault);
        addressEntity.setCreatedDate(new Date());
        addressEntity.setLastModifiedDate(new Date());
        return addressEntity;
    }

    public static AddressRequest entityToRequest(AddressEntity addressEntity){
        AddressRequest addressRequest = new AddressRequest();
        if(addressEntity.getId() != null){
            addressRequest.setId(addressEntity.getId());
        }
        addressRequest.setFullName(addressEntity.getFullName());
        addressRequest.setAddressLineOne(addressEntity.getAddressLineOne());
        addressRequest.setAddressLineTwo(addressEntity.getAddressLineTwo());
        addressRequest.setState(addressEntity.getState());
        addressRequest.setCity(addressEntity.getCity());
        addressRequest.setPincode(addressEntity.getPincode());
        addressRequest.setIsDefault(addressEntity.getIsDefault());
        addressRequest.setCreatedDate(addressEntity.getCreatedDate());
        addressRequest.setLastModifyDate(addressEntity.getLastModifiedDate());
        return addressRequest;
    }
}
