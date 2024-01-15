package com.example.buildingproject.controller;
import com.example.buildingproject.dtos.request.HouseCreateDTO;
import com.example.buildingproject.dtos.response.HouseResponseDTO;
import com.example.buildingproject.service.house.HouseService;
import com.example.buildingproject.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/house")
public class HouseController {
    private final HouseService houseService ;
    private final UserService userService ;



    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<String> create(@Valid @RequestBody HouseCreateDTO houseCreateDTO){
        System.out.println(houseCreateDTO.getHomeAddress());
        String result = houseService.create(houseCreateDTO);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/getOwnerHousesByPassportNo")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ArrayList<HouseResponseDTO>> getOwnerHousesByPassportNo(@Valid @RequestParam String passportNo, Principal principal){
        String name = principal.getName();
        System.out.println("name = " + name);
        ArrayList<HouseResponseDTO> houseResponseDTOS = houseService.getOwnerHouses(passportNo);

        return ResponseEntity.ok(houseResponseDTOS);
    }

    @PostMapping("/getOwnerHousesByPhoneNumber")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ArrayList<HouseResponseDTO>> getOwnerHousesByPhoneNumber(@Valid @RequestParam String passportPhoneNumber){
        ArrayList<HouseResponseDTO> houseResponseDTOS = houseService.getOwnerHousesByPhoneNumber(passportPhoneNumber);
        return ResponseEntity.ok(houseResponseDTOS);
    }


    @PostMapping("/changeOwnerOfTheHouse")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<HouseResponseDTO> changeOwnerOfTheHouse(@Valid @RequestParam UUID houseId
            , @RequestParam String newOwnerPassportNo ){

        HouseResponseDTO houseResponseDTOS = houseService.changeOwnerOfTheHouse(houseId , newOwnerPassportNo);
        return ResponseEntity.ok(houseResponseDTOS);
    }





}
