package com.example.buildingproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HouseEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    private UserEntity ownerHouse ;
    @Column(nullable = false)
    private int contractNumber ; //Shartnoma raqami
    @Column(unique = true)
    private String homeAddress ;

    private double housePrice ;     //uy narhi

    private LocalDateTime contractExpirationTime ; //shartnoma tugash vaqti
}
