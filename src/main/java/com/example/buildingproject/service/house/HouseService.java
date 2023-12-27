package com.example.buildingproject.service.house;

import com.example.buildingproject.dtos.request.HouseCreateDTO;
import com.example.buildingproject.dtos.response.HouseResponseDTO;

import java.util.ArrayList;
import java.util.UUID;

public interface HouseService {
    String create(HouseCreateDTO houseCreateDTO);


    ArrayList<HouseResponseDTO> getOwnerHouses(String passportNo);


    ArrayList<HouseResponseDTO> getOwnerHousesByPhoneNumber(String passportNo);

}
