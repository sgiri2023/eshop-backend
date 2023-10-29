package com.example.eshopbackend.eshopbackend.session;

import com.example.eshopbackend.eshopbackend.datamodel.SessionDataModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface SessionStore {
    public static Map<String, SessionDataModel> SESSION_TRACKER=new ConcurrentHashMap<>();
}
