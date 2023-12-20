package com.example.buildingproject.service.history;

import com.example.buildingproject.dtos.request.HistoryRequest;
import com.example.buildingproject.dtos.response.HistoryResponse;

import java.util.ArrayList;
import java.util.UUID;


public interface HistoryService {
    String paid(HistoryRequest historyRequest);

    ArrayList<HistoryResponse> unPaid(UUID userId, UUID houseId);

    ArrayList<HistoryResponse> getUnpaidHistoryEntitiesForYearAndMonth(int month, int year);

}
