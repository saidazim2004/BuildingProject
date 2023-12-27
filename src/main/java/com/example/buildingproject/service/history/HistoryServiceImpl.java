package com.example.buildingproject.service.history;

import com.example.buildingproject.dtos.request.HistoryRequest;
import com.example.buildingproject.dtos.response.HistoryResponse;
import com.example.buildingproject.entity.HistoryEntity;
import com.example.buildingproject.exceptions.DataNotFoundException;
import com.example.buildingproject.exceptions.EnterWrongSum;
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
            throw new DataNotFoundException("Hamma mijozlar qardorligini to'lab bo'lishgan !");
        }else {
            return modelMapper.map(historyResponses , new TypeToken<ArrayList<HistoryResponse>>(){}.getType());
        }
    }

    @Override
    public ArrayList<HistoryResponse> getUnpaidHistoryEntitiesForYearAndMonth(int month, int year) {
        List<HistoryEntity> historyEntities = historyRepository.getUnpaidHistoryEntitiesYearAndForMonth(month, year);
        if (historyEntities.isEmpty()){
            throw new DataNotFoundException("Kiritilgan vaqt uchun mijozlar to'lovlarni amalga oshirib bo'lishgan");
        }
        return modelMapper.map(historyEntities , new TypeToken<List<HistoryResponse>>(){}.getType());

    }

    @Override
    public String paid(HistoryRequest historyRequest) {

        Optional<HistoryEntity> history = historyRepository.findById(historyRequest.getHistoryId());

        if (historyRequest.getSum()>history.get().getAmountPayableMonthly()){
            throw new EnterWrongSum("Oylik miqdordan kop kirita olmaysiz : ("+history.get().getAmountPayableMonthly()+") oylik miqdor");
        }
        history.get().setAmountPaid(history.get().getAmountPaid()+historyRequest.getSum());
        history.get().setIndebtednessSum(history.get().getIndebtednessSum()-historyRequest.getSum());

        if (history.get().getIndebtednessSum()==0){
            history.get().setPaid(true);
        }

        historyRepository.save(history.get());


        return "Success paid !";
    }

    @Override
    public ArrayList<HistoryResponse> getAllHistories(UUID userId, UUID houseId) {

        ArrayList<HistoryEntity> historyEntities = historyRepository.getAllHistories(userId,houseId);

        if (historyEntities.isEmpty()){
            throw new DataNotFoundException("Hisobotlar topilmadi");
        }
        else {
           return modelMapper.map(historyEntities , new TypeToken<ArrayList<HistoryResponse>>(){}.getType());
        }
    }
}
