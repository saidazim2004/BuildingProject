package com.example.buildingproject.service.history;

import com.example.buildingproject.dtos.request.HistoryRequest;
import com.example.buildingproject.dtos.response.HistoryResponse;
import com.example.buildingproject.entity.HistoryEntity;
import com.example.buildingproject.exceptions.DataNotFoundException;
import com.example.buildingproject.repository.HistoryRepository;
import com.example.buildingproject.repository.HouseRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService{
    private final HistoryRepository historyRepository ;
    private final ModelMapper modelMapper ;



    @Override
    public ArrayList<HistoryResponse> unPaid(UUID userId, UUID houseId) {
        ArrayList<HistoryEntity> historyResponses = historyRepository.getHistoryEntitiesByIdAndOwnerId(userId , houseId);

        if (historyResponses.isEmpty()){
            throw new DataNotFoundException("All payments are made by the customer");
        }else {
            return modelMapper.map(historyResponses , new TypeToken<ArrayList<HistoryResponse>>(){}.getType());
        }
    }
    @Override
    public ArrayList<HistoryResponse> getUnpaidHistoryEntitiesForYearAndMonth(int month, int year) {
        List<HistoryEntity> historyEntities = historyRepository.getUnpaidHistoryEntitiesYearAndForMonth(month, year);
        if (historyEntities.isEmpty()){
            throw new DataNotFoundException("Payments have been made for the time entered");
        }
        return modelMapper.map(historyEntities , new TypeToken<List<HistoryResponse>>(){}.getType());

    }

    @Override
    public String paid(HistoryRequest historyRequest) {


        Optional<HistoryEntity> history = historyRepository.findById(historyRequest.getHouseId());


        

        history.get().setAmountPaid(history.get().getAmountPaid()+historyRequest.getSum());
        history.get().setIndebtednessSum(history.get().getIndebtednessSum()-historyRequest.getSum());

        if (history.get().getIndebtednessSum()==0){
            history.get().setPaid(true);
        }

        historyRepository.save(history.get());


        return "Success paid !";
    }
}
