package com.example.buildingproject.controller;

import com.example.buildingproject.dtos.request.LoginDTO;
import com.example.buildingproject.dtos.request.ResetPasswordDto;
import com.example.buildingproject.dtos.request.UpdatePasswordDto;
import com.example.buildingproject.dtos.request.UserCreateDto;

import com.example.buildingproject.dtos.response.AuthResponseDTO;
import com.example.buildingproject.dtos.response.TokenDTO;
import com.example.buildingproject.dtos.response.UserResponseDTO;
import com.example.buildingproject.dtos.response.VerifyDTO;
import com.example.buildingproject.service.auth.AuthServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthServiceImpl authService ;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponseDTO<UserResponseDTO>> create(@Valid @RequestBody UserCreateDto userCreateDto){
        System.out.println("userCreateDto.getEmail() = " + userCreateDto.getEmail());
        AuthResponseDTO<UserResponseDTO> responseDTO = authService.create(userCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(
            @Valid @RequestBody VerifyDTO verifyDTO
    ) {
        System.out.println("verifyDTO.getEmail() = " + verifyDTO.getEmail());
        String response = authService.verify(verifyDTO.getEmail(), verifyDTO.getVerificationCode());
        return ResponseEntity.ok(response);
    }
    @PostMapping("/new-verification-code")
    public ResponseEntity<String> newVerificationCode(@Valid @RequestParam String email){
        String res = authService.newVerifyCode(email);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<TokenDTO> signIn(@Valid @RequestBody LoginDTO loginDTO){
        TokenDTO tokenDTO = authService.signIn(loginDTO);
        return ResponseEntity.ok(tokenDTO);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestParam @Email String email){
        String response = authService.forgotPassword(email);
        return ResponseEntity.ok(response);

    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordDto resetPassword){

        String response = authService.resetPassword(resetPassword);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto){
        String response = authService.updatePassword(updatePasswordDto);
        return ResponseEntity.ok(response);
    }



}
