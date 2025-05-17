package com.example.question_service.service.client;

import org.springframework.stereotype.Component;

@Component
public class UserClient {

    /**  TODO: Call User Service (REST/Feign).  For now always true. */
    public boolean doesUserExist(String userId) {
        return true;          // replace with real check later
    }
}