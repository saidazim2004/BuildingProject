package com.example.buildingproject.service.house;

import com.example.buildingproject.dtos.request.HouseCreateDTO;
import com.example.buildingproject.dtos.response.HouseResponseDTO;
import com.example.buildingproject.entity.HistoryEntity;
import com.example.buildingproject.entity.HouseEntity;
import com.example.buildingproject.entity.UserEntity;
import com.example.buildingproject.exceptions.DataNotFoundException;
import com.example.buildingproject.exceptions.HouseWasBoughtException;
import com.example.buildingproject.repository.HistoryRepository;
import com.example.buildingproject.repository.HouseRepository;
import com.example.buildingproject.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Period;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {
    private final UserRepository userRepository ;
    private final HouseRepository houseRepository ;
    private final ModelMapper modelMapper ;
    private final HistoryRepository historyRepository ;




    @Override
    public String create(HouseCreateDTO houseCreateDTO) {

        Optional<HouseEntity> house = houseRepository.findHouseEntityByHomeAddress(houseCreateDTO.getHomeAddress());
        if (house.isPresent()){
            throw new HouseWasBoughtException("Bu uy allaqachon sotib bo'lingan");
        } else {
            Optional<UserEntity> user = userRepository.getUserEntityByPassportNo(houseCreateDTO.getPassportNo());

            if (user.isPresent()){

                HouseEntity houseMap = modelMapper.map(houseCreateDTO, HouseEntity.class);
                houseMap.setOwnerHouse(user.get());

                houseRepository.save(houseMap);

                LocalDateTime currentMonth = LocalDateTime.now();
                while (currentMonth.isBefore(houseMap.getContractExpirationTime()) || currentMonth.isEqual(houseMap.getContractExpirationTime())) {
                    HistoryEntity historyEntity = HistoryEntity.builder()
                            .user(houseMap.getOwnerHouse())
                            .house(houseMap)
                            .amountPayableMonthly(houseCreateDTO.getAmountPayableMonthly())
                            .indebtednessSum(houseCreateDTO.getAmountPayableMonthly())
                            .amountPaid(0)
                            .isPaid(false)
                            .monthlyPeriod(currentMonth)
                            .build();

                    historyRepository.save(historyEntity);


                    currentMonth = currentMonth.plusMonths(1);
                }

                return "Success added house !" ;
            }

            else {
                throw new DataNotFoundException("Foydalanuvchi topilmadi passport seria raqamini qayta kirgazing !");
            }
        }

    }

    @Override
    public HouseResponseDTO changeOwnerOfTheHouse(UUID houseId, String newOwnerPassportNo) {

        Optional<UserEntity> newUserEntityByPassportNo = userRepository.getUserEntityByPassportNo(newOwnerPassportNo);

        if (newUserEntityByPassportNo.isEmpty()){
            throw new DataNotFoundException("Hatolik , uyning yangi egasini passport raqamini tekshirib kirgazing !");
        }
        else {
            Optional<HouseEntity> houseById = houseRepository.findById(houseId);
            if (houseById.isEmpty()){
                throw new DataNotFoundException("Uy topilmadi");
            }else {
                houseById.get().setOwnerHouse(newUserEntityByPassportNo.get());

                ArrayList<HistoryEntity> historyEntitiesByHouseId = historyRepository.getHistoryEntitiesByHouseId(houseById.get().getId());
                for (HistoryEntity history : historyEntitiesByHouseId) {
                    if (history.getHouse().getId().equals(houseId)){
                        history.setUser(newUserEntityByPassportNo.get());
                        historyRepository.save(history);
                    }
                }

                HouseEntity save = houseRepository.save(houseById.get());
                return modelMapper.map(save , HouseResponseDTO.class);
            }
        }
    }

    @Override
    public ArrayList<HouseResponseDTO> getOwnerHousesByPhoneNumber(String phoneNum) {
        Optional<UserEntity> user = userRepository.getUserEntityByPhoneNumber(phoneNum);
        if (user.isEmpty()){
            throw new DataNotFoundException("Foydalanuvchi topilmadi qaytadan nomerni tekshirib kiritib koring ");
        }
        return getUserHouses(user,phoneNum);
    }



    @Override
    public ArrayList<HouseResponseDTO> getOwnerHouses(String passwordNo) {
        Optional<UserEntity> user = userRepository.getUserEntityByPassportNo(passwordNo);
        if (user.isEmpty()){
            throw new DataNotFoundException("Foydalanuvchi topilmadi qaytadan nomerni tekshirib kiritib koring ");
        }
        return getUserHouses(user,passwordNo);
    }




    private int expirationMonth(LocalDateTime contractExpirationTime) {

        System.out.println("contractExpirationTime = " + contractExpirationTime);

        LocalDateTime currentTime = LocalDateTime.now();
        Period period = Period.between(currentTime.toLocalDate(), contractExpirationTime.toLocalDate());


        System.out.println("MONTH MONTH MONTH: " + period.getMonths());
        return period.getMonths();

    }

    private ArrayList<HouseResponseDTO> getUserHouses(Optional<UserEntity> user, String request) {
        if (user.isPresent()){
            ArrayList<HouseEntity> houseEntities = houseRepository.findHouseEntitiesByOwnerId(user.get().getId());
            if (houseEntities.isEmpty()){
                throw new DataNotFoundException("Bu foydalanuvchiga tegishli uylar mavjud emas");
            }else {
                return modelMapper.map(houseEntities, new TypeToken<ArrayList<HouseResponseDTO>>() {
                }.getType());

            }
        }
        else {
            throw new DataNotFoundException("Raqamingizni tekshiring hech nima topilmadi : "+request);
        }
    }



}
