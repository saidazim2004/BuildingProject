package com.example.buildingproject.dtos.request;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HouseCreateDTO {
    private String passportNo ;
    private double housePrice;
    private double amountPayableMonthly ;//har oy tolanadigan summa
    private int contractNumber ; //Shartnoma raqami
    private String homeAddress ;
    private LocalDateTime contractExpirationTime ;


}
