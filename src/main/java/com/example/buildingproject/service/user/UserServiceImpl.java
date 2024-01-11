package com.example.buildingproject.service.user;

import com.example.buildingproject.dtos.request.UserCreateDto;
import com.example.buildingproject.dtos.response.UserResponseDTO;
import com.example.buildingproject.entity.UserEntity;
import com.example.buildingproject.enums.UserRole;
import com.example.buildingproject.enums.UserStatus;
import com.example.buildingproject.exceptions.DataNotFoundException;
import com.example.buildingproject.exceptions.UserAlreadyExistException;
import com.example.buildingproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.buildingproject.enums.UserStatus.ACTIVE;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository ;
    private final ModelMapper modelMapper ;
    @Override
    public UserEntity getUserByID(UUID userID) {
        return userRepository.getUserEntityById(userID);
    }

    @Override
    public UserResponseDTO getById(UUID id) {
        return null;
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new DataNotFoundException("User not found with Email: " + email)
        );
    }

    @Override
    public List<UserResponseDTO> getAll(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "firstName");
        Pageable pageable = PageRequest.of(page, size, sort);
        List<UserEntity> users = userRepository.findAllUsers(pageable).getContent();
        return modelMapper.map(users, new TypeToken<List<UserResponseDTO>>() {
        }.getType());
    }

    @Override
    public String unblockByID(UUID userId) {
        UserEntity user = findByID(userId);
        if(user.getStatus().equals(ACTIVE)) {
            return "User already unblocked";
        }
        user.setStatus(ACTIVE);
        userRepository.save(user);
        return "User unblocked";
    }

    @Override
    public String deleteByID(UUID userId) {
        UserEntity user = findByID(userId);
        user.setDeleted(true);
        userRepository.save(user);
        return "user deleted";
    }

    @Override
    public UserResponseDTO createClient(UserCreateDto userCreateDto) {
        Optional<UserEntity> userEntityByPassportNo = userRepository.getUserEntityByPassportNo(userCreateDto.getPassportNo());
        Optional<UserEntity> userEntityByPhoneNumber = userRepository.getUserEntityByPhoneNumber(userCreateDto.getPhoneNumber());
        if (userEntityByPhoneNumber.isEmpty() && userEntityByPassportNo.isEmpty()){
            UserEntity map = modelMapper.map(userCreateDto, UserEntity.class);
            map.setRole(UserRole.USER);
            map.setStatus(ACTIVE);
            userRepository.save(map);
            return modelMapper.map(map , UserResponseDTO.class);
        }
        else {
            throw new UserAlreadyExistException("Bu foydalanuchi avval jam royhatdan o'tkazilgan");
        }
    }

    @Override
    public String updatePhoneNumber(String oldPhoneNumber, String newPhoneNumber) {

        Optional<UserEntity> user = userRepository.getUserEntityByPhoneNumber(oldPhoneNumber);
        if(user.isEmpty()){
            throw new DataNotFoundException("Foydalanuvchining eski telefon raqamini tekshirib kirgazing !");
        }
        else{
            user.get().setPhoneNumber(newPhoneNumber);
            userRepository.save(user.get());
            return "Mijoning telefon raqami muaffaqiyatli o'zgartirildi" ;
        }

    }

    @Override
    public UserResponseDTO getByPhoneNumber(String phoneNumber) {
        Optional<UserEntity> userEntityByPhoneNumber = userRepository.getUserEntityByPhoneNumber(phoneNumber);

        if (userEntityByPhoneNumber.isEmpty()){
            throw new DataNotFoundException("Foydalanuvchi topilmadi telefon raqamini qayta kiriting : " +phoneNumber);
        }
        else {
            return modelMapper.map(userEntityByPhoneNumber, UserResponseDTO.class);
        }
    }

    @Override
    public void deleteSelectedUsers(List<UUID> userIds) {

    }

    private UserEntity findByID(UUID id) {
        return userRepository.getUserByID(id).orElseThrow(
                () -> new DataNotFoundException("user not found with ID: " + id));
    }
}
