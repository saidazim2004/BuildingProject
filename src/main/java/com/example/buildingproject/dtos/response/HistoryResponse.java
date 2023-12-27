package com.example.buildingproject.dtos.response;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HistoryResponse {

    private UUID id ;
    private UUID userId ;

    private UUID houseId ;
    private double amountPaid ;          //bitta oy uchun tolagan summasi
    private double amountPayableMonthly ;//har oy tolanadigan summa
    private double indebtednessSum ;//qarzdorlik summasi
    private LocalDateTime updatedDate;
    private LocalDateTime monthlyPeriod ;
}
