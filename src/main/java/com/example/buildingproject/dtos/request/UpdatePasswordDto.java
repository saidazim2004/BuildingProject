package com.example.buildingproject.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter

public class UpdatePasswordDto {
    private UUID userId ;
    private String oldPassword ;
    private String newPassword ;
    private String repeatPassword ;

}
