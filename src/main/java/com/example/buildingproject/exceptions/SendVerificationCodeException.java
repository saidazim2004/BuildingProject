package com.example.buildingproject.exceptions;


public class SendVerificationCodeException extends RuntimeException {
    public SendVerificationCodeException(String message) {
        super(message);
    }
}
