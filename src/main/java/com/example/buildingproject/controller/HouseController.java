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


    @PostMapping("/getOwnerHouses")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ArrayList<HouseResponseDTO>> getOwnerHouses(@RequestParam String passportNo){
         ArrayList<HouseResponseDTO> houseResponseDTOS = houseService.getOwnerHouses(passportNo);
         return ResponseEntity.ok(houseResponseDTOS);
    }





}
