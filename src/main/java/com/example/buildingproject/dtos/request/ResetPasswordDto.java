package com.example.buildingproject.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResetPasswordDto {
    @Email
    @NotBlank(message = "Email must not be blank")
    private String email ;
    @NotBlank(message = "VerificationCode must not be blank")
    private String verificationCode ;
    @NotBlank(message = "New password must not be blank")
    private String newPassword ;
    @NotBlank(message = "Confirm password must not be blank")
    private String confirmPassword ;
}

