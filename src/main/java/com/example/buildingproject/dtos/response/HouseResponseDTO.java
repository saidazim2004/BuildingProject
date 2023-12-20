package com.example.buildingproject.dtos.response;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HouseResponseDTO {
    private UUID id ;
    private UUID ownerHouseId ;
    private double housePrice ;
    @Column(unique = true)
    private String homeAddress ;


}
