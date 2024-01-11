package com.example.buildingproject.controller;

import com.example.buildingproject.dtos.request.UserCreateDto;
import com.example.buildingproject.dtos.response.UserResponseDTO;

import com.example.buildingproject.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/updatePhoneNumber")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<String> updatePhoneNumber(@Valid @RequestParam String oldPhoneNumber, @RequestParam String newPhoneNumber){
        String result = userService.updatePhoneNumber(oldPhoneNumber , newPhoneNumber);
        return ResponseEntity.ok(result);
    }

    @PostMapping("createClient")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<UserResponseDTO> createClient(@Valid @RequestBody UserCreateDto userCreateDto){
        UserResponseDTO userResponseDTO = userService.createClient(userCreateDto);
        return ResponseEntity.ok(userResponseDTO);

    }



}
