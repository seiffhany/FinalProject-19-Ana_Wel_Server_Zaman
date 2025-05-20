package com.example.question_service.service.client;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.question_service.dto.UserDTO;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserClient {
    @GetMapping("/profile/communication/{userId}")
    UserDTO getUserById(@PathVariable("userId") UUID userId);

    @GetMapping("/profile/followers/{userId}")
    List<UserDTO> getUserFollowers(@PathVariable("userId") UUID userId);
}