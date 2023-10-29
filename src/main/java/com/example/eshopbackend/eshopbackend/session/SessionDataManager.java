package com.example.eshopbackend.eshopbackend.session;

import com.example.eshopbackend.eshopbackend.datamodel.SessionDataModel;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SessionDataManager implements SessionStore{
    public void setSessionDataAfterLogin(String key, Long userId, String userName, String token) {
        SessionDataModel sessionData = new SessionDataModel();
        if (SESSION_TRACKER.get(key) == null) {
            if (userId != null) {
                sessionData.setUserId(userId);
            }
            if (userName != null) {
                sessionData.setUserName(userName);
            }
            if (token != null) {
                sessionData.setToken(token);
            }
            sessionData.setLastActivityTime(new Date().getTime());
            SESSION_TRACKER.put(key, sessionData);
        }
    }
}
