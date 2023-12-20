package com.example.buildingproject.entity;

import jakarta.persistence.Entity;

import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class HistoryEntity extends BaseEntity {

    @ManyToOne
    private UserEntity user ;
    @ManyToOne
    private HouseEntity house ;

    private double amountPaid ;          //bitta oy uchun tolagan summasi
    private double amountPayableMonthly ;//har oy tolanadigan summa
    private double indebtednessSum ;      //qarzdorlik summasi
    private boolean isPaid ;             //tolov toliq qilinganmi yoqmi
    private LocalDateTime monthlyPeriod ;

}
