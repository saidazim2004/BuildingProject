package com.example.buildingproject.dtos.request;


import lombok.*;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class HistoryRequest {

    private UUID historyId ;
    private double sum;
}
