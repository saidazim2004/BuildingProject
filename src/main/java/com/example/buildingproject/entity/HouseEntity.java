package com.example.buildingproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HouseEntity extends BaseEntity {
    @ManyToOne
    private UserEntity ownerHouse ;
    @Column(nullable = false)
    private int contractNumber ; //Shartnoma raqami
    @Column(unique = true)
    private String homeAddress ;

    private double housePrice ;     //uy narhi

    private LocalDateTime contractExpirationTime ; //shartnoma tugash vaqti
}
