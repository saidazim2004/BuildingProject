package com.example.buildingproject.controller;

import com.example.buildingproject.dtos.request.HistoryRequest;
import com.example.buildingproject.dtos.response.HistoryResponse;
import com.example.buildingproject.dtos.response.HouseResponseDTO;
import com.example.buildingproject.service.history.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/history")
public class HistoryController {

    private final HistoryService historyService ;

    @PostMapping("/unpaidHistories")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ArrayList<HistoryResponse>> unpaid(@RequestParam UUID userId , @RequestParam UUID houseId){

        ArrayList<HistoryResponse> historyResponses = historyService.unPaid(userId , houseId);

        return ResponseEntity.ok(historyResponses);
    }


    @PostMapping("/getUnPaidHistoriesYearAndMonth")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ArrayList<HistoryResponse>> getUnpaidYearAndMonth(@RequestParam int month , @RequestParam int year){

        ArrayList<HistoryResponse> historyResponses = historyService.getUnpaidHistoryEntitiesForYearAndMonth(month , year);

        return ResponseEntity.ok(historyResponses);
    }



    @PutMapping("/paid")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<String> paid(@RequestBody HistoryRequest historyRequest){

        String result = historyService.paid(historyRequest);

        return ResponseEntity.ok(result);
    }
}
