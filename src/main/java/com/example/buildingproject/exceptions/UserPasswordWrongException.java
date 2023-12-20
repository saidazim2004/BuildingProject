package com.example.buildingproject.exceptions;

import jakarta.validation.constraints.NotBlank;

public class UserPasswordWrongException extends RuntimeException {
    public UserPasswordWrongException(String message) {
        super(message);
    }
}
