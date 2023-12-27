package com.example.buildingproject.controller;

import com.example.buildingproject.dtos.response.UserResponseDTO;

import com.example.buildingproject.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService ;

    @GetMapping("/getByPhoneNumber")
    public ResponseEntity<UserResponseDTO> getByPhoneNumber(@Valid @RequestParam String phoneNumber){
        System.out.println(phoneNumber);
        UserResponseDTO userResponseDTO = userService.getByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(userResponseDTO);
    }
}
