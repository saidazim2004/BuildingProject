package com.example.buildingproject.service.auth;


import com.example.buildingproject.config.jwt.JwtService;
import com.example.buildingproject.dtos.request.LoginDTO;
import com.example.buildingproject.dtos.request.ResetPasswordDto;
import com.example.buildingproject.dtos.request.UpdatePasswordDto;
import com.example.buildingproject.dtos.request.UserCreateDto;
import com.example.buildingproject.dtos.response.AuthResponseDTO;
import com.example.buildingproject.dtos.response.TokenDTO;
import com.example.buildingproject.dtos.response.UserResponseDTO;
import com.example.buildingproject.entity.UserEntity;
import com.example.buildingproject.entity.VerificationData;
import com.example.buildingproject.enums.UserRole;
import com.example.buildingproject.enums.UserStatus;
import com.example.buildingproject.exceptions.BadRequestException;
import com.example.buildingproject.exceptions.DuplicateValueException;
import com.example.buildingproject.exceptions.UserPasswordWrongException;
import com.example.buildingproject.repository.UserRepository;
import com.example.buildingproject.service.mail.MailSenderService;
import com.example.buildingproject.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final MailSenderService mailSenderService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final Map<String, UserEntity> userMap = new HashMap<>();

    @Override
    public AuthResponseDTO<UserResponseDTO> create(UserCreateDto userCreateDto) {
        checkEmailUnique(userCreateDto.getEmail());
        checkUserPasswordAndIsValid(userCreateDto.getPassword(), userCreateDto.getConfirmPassword());
        UserEntity user = modelMapper.map(userCreateDto, UserEntity.class);

        user.setRole(UserRole.USER);
        user.setStatus(UserStatus.UNVERIFIED);
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setVerificationData(generateVerificationData());

        userMap.put(userCreateDto.getEmail(), user);
        String message = mailSenderService.sendVerificationCode(user.getEmail(),
                user.getVerificationData().getVerificationCode());
        UserResponseDTO mappedUser = modelMapper.map(user, UserResponseDTO.class);
        return new AuthResponseDTO<>(message, mappedUser);
    }

    @Override
    public String verify(String email, String verificationCode) {
        UserEntity user = userMap.get(email);
        if (checkVerificationCodeAndExpiration(user.getVerificationData(), verificationCode))
            return "Verification code wrong";
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        userMap.remove(email);
        return "Successfully verified";
    }




    @Override
    public String newVerifyCode(String email) {
        UserEntity user = userService.getUserByEmail(email);
        if (user != null) {
            user.setVerificationData(generateVerificationData());
            userRepository.save(user);
            return mailSenderService.sendVerificationCode(email, user.getVerificationData().getVerificationCode());
        } else {
            UserEntity mapUser = userMap.get(email);
            mapUser.setVerificationData(generateVerificationData());
            return mailSenderService.sendVerificationCode(email, mapUser.getVerificationData().getVerificationCode());
        }

    }


    @Override
    public TokenDTO signIn(LoginDTO loginDTO) {
        UserEntity user = getActiveUserByEmail(loginDTO.getEmail());

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())){
            throw new UserPasswordWrongException("User password wrong with Password: " + loginDTO.getPassword());
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );
        return jwtService.generateToken(user.getEmail());
    }
    @Override
    public String forgotPassword(String email) {
        UserEntity user = getActiveUserByEmail(email);
        user.setVerificationData(generateVerificationData());
        userRepository.save(user);
        return mailSenderService.sendVerificationCode(email , user.getVerificationData().getVerificationCode());
    }



    @Override
    public String resetPassword(ResetPasswordDto resetPasswordDto) {
        UserEntity user = getActiveUserByEmail(resetPasswordDto.getEmail());

        if(checkVerificationCodeAndExpiration(user.getVerificationData(),resetPasswordDto.getVerificationCode())){
            return "Verification code wrong !";
        }
        checkUserPasswordAndIsValid(resetPasswordDto.getNewPassword(), resetPasswordDto.getConfirmPassword());
        user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
        userRepository.save(user);
        return "Password successfully changed";

    }




    @Override
    public String updatePassword(UpdatePasswordDto updatePasswordDto) {

        UserEntity user = userService.getUserByID(updatePasswordDto.getUserId());
        if (!passwordEncoder.matches(updatePasswordDto.getOldPassword(),user.getPassword())){
            throw new UserPasswordWrongException("Old password wrong! Password: " + updatePasswordDto.getOldPassword());
        }
        else {
            checkUserPasswordAndIsValid(updatePasswordDto.getNewPassword(), updatePasswordDto.getRepeatPassword());
            user.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
            userRepository.save(user);
            return "Password successfully updated";
        }
    }

    public boolean checkVerificationCodeAndExpiration(VerificationData verificationData, String verificationCode) {
        if (!verificationData.getVerificationDate().plusMinutes(100).isAfter(LocalDateTime.now()))
            throw new BadRequestException("Verification code expired");
        return !Objects.equals(verificationData.getVerificationCode(), verificationCode);
    }
    private void checkEmailUnique(String email) {
        if (userRepository.existsUserByEmail(email))
            throw new DuplicateValueException("User already exists with Email: " + email);
    }
    private void checkUserPasswordAndIsValid(String password, String confirmPassword) {
        if (!Objects.equals(password, confirmPassword))
            throw new UserPasswordWrongException("Password must be same with confirm password: " + confirmPassword);
        checkPasswordIsValid(password);
    }

    private void checkPasswordIsValid(String password) {
        String passwordRegex = "^(?=.*[a-zA-Z])(?=.*\\d).{8,20}$";
        if (!password.matches(passwordRegex)) {
            throw new IllegalArgumentException("Invalid password: " + password);
        }
    }
    private VerificationData generateVerificationData() {
        Random random = new Random();
        String verificationCode = String.valueOf(random.nextInt(100000, 1000000));
        return new VerificationData(verificationCode, LocalDateTime.now());
    }

    private UserEntity getActiveUserByEmail(String email) {
        UserEntity user = userService.getUserByEmail(email);
        if (user.getStatus().equals(UserStatus.UNVERIFIED)) {
            throw new BadRequestException("User unverified");
        }
        return user;

    }


}
