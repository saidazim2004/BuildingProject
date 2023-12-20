package com.example.buildingproject.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationData {

    private String verificationCode;

    private LocalDateTime verificationDate;

}
