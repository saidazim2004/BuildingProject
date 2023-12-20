package com.example.buildingproject.service.user;

import com.example.buildingproject.dtos.response.UserResponseDTO;
import com.example.buildingproject.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserEntity getUserByID(UUID userID);
    UserResponseDTO getById(UUID id);
    UserEntity getUserByEmail(String email);
    List<UserResponseDTO> getAll(Integer page, Integer size);
    String unblockByID(UUID userId);
    String deleteByID(UUID userId);
    void deleteSelectedUsers(List<UUID> userIds);

    UserResponseDTO getByPhoneNumber(String phoneNumber);


}
