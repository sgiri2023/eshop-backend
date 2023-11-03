package com.example.eshopbackend.eshopbackend.controller;

import com.example.eshopbackend.eshopbackend.datamodel.*;
import com.example.eshopbackend.eshopbackend.service.impl.UserServiceImpl;
import com.example.eshopbackend.eshopbackend.session.SessionDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.example.eshopbackend.eshopbackend.session.SessionStore.SESSION_TRACKER;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserServiceImpl userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SessionDataManager sessionDataManager;

    // http://localhost:8090/api/user/info
    @GetMapping("/info")
    public Object userAppInfo(){
        return "User Controller";
    }

    // http://localhost:8090/api/user/login
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest authRequest) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
            System.out.println("Authentication: " + authentication);
            if(authentication.isAuthenticated()){
                String sessionKey = "spring-" + UUID.randomUUID().toString().substring(28) + "-security";
                UserResponse loggedInUser = userService.getUserByUserName(authRequest.getEmail());
                sessionDataManager.setSessionDataAfterLogin(sessionKey, loggedInUser.getId(), loggedInUser.getEmail(), "custom-token");
                SessionDataModel sessionData = SESSION_TRACKER.get(sessionKey);
                System.out.println("Session Details: " + sessionData);
                return new ResponseEntity<String>(sessionKey, HttpStatus.OK);
            }
        } catch(AuthenticationException e){
            System.out.println("error: " + e.toString());
            throw new Exception(e.getMessage());
        }
        String sessionKey = "spring-" + UUID.randomUUID().toString().substring(28) + "-security";
        return null;
    }

    // http://localhost:8090/api/user/logout
    @GetMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void logout(@RequestHeader String Authorization) {
        if (SESSION_TRACKER.containsKey(Authorization)) {
            SESSION_TRACKER.remove(Authorization);
        }
    }

    // http://localhost:8090/api/user/add
    @PostMapping("/add")
    public UserResponse addUser(@RequestBody UserRequest userRequest){
        System.out.println("Add user Request: " + userRequest);
        return userService.addUser(userRequest);
    }

    // http://localhost:8090/api/user/list
    @GetMapping("/list")
    public List<UserResponse> userList(){
        System.out.println("Get User List");
        return userService.getUserList();
    }

    // http://localhost:8090/api/user/buyer-seller/list
    @GetMapping("/buyer-seller/list")
    public ResponseEntity<?> userListBuyerSeller(@RequestHeader String Authorization){
        System.out.println("Inside User Details Controller");
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        System.out.println("Get User List");
        return new ResponseEntity<>(userService.getAllSellerAndBuyerUserList(), HttpStatus.OK);
    }

    // http://localhost:8090/api/user/details
    @GetMapping("/details")
    public Object getUserDetails(@RequestHeader String Authorization){
        System.out.println("Inside User Details Controller");
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return "Session Timeout";
        }
        System.out.println("Session Details: " + sessionData);
        return userService.getUserByUserName(sessionData.getUserName());
    }

    // Add User Address
    // http://localhost:8090/api/user/add-address
    @PostMapping("/add-address")
    public ResponseEntity<?> addUserAddress(@RequestHeader String Authorization, @RequestBody AddressRequest addressRequest){
        System.out.println("Add address Request: " + addressRequest);
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        List<AddressRequest> addressList = userService.addUserAddress(sessionData.getUserId(), addressRequest);
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    // Get Address
    // http://localhost:8090/api/user/get-address-list
    @GetMapping("/get-address-list")
    public ResponseEntity<?> getUserAddress(@RequestHeader String Authorization){
        System.out.println("Get user address List: " + Authorization);
        SessionDataModel sessionData = SESSION_TRACKER.get(Authorization);
        if (sessionData == null) {
            return new ResponseEntity<>("Access denied", HttpStatus.BAD_REQUEST);
        }
        List<AddressRequest> addressList = userService.getUserAddressList(sessionData.getUserId());
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }
}
