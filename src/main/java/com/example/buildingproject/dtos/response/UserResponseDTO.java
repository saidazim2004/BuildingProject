package com.example.buildingproject.dtos.response;

import com.example.buildingproject.enums.UserRole;
import com.example.buildingproject.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
    private UUID userId ;
    private String firstName;

    private String lastName;

    private String email;

    @JsonIgnore
    private String password;

    private UserRole role;

    private UserStatus status;

    private Long mediaId;
}
