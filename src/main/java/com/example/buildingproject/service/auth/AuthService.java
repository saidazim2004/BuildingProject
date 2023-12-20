package com.example.buildingproject.service.auth;

import com.example.buildingproject.dtos.request.LoginDTO;
import com.example.buildingproject.dtos.request.ResetPasswordDto;
import com.example.buildingproject.dtos.request.UpdatePasswordDto;
import com.example.buildingproject.dtos.request.UserCreateDto;

import com.example.buildingproject.dtos.response.AuthResponseDTO;
import com.example.buildingproject.dtos.response.TokenDTO;
import com.example.buildingproject.dtos.response.UserResponseDTO;


public interface AuthService {
    AuthResponseDTO<UserResponseDTO> create(UserCreateDto userCreateDto);
    String verify(String email, String verificationCode);

    String newVerifyCode(String email);

    TokenDTO signIn(LoginDTO loginDTO);

    String forgotPassword(String email);
    String resetPassword(ResetPasswordDto resetPasswordDto);

    String updatePassword(UpdatePasswordDto updatePasswordDto);




}
