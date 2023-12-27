package com.example.buildingproject.exceptionHandler;

import com.example.buildingproject.dtos.response.AppErrorDTO;
import com.example.buildingproject.exceptions.DataNotFoundException;
import com.example.buildingproject.exceptions.UserPasswordWrongException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<AppErrorDTO> dataNotFoundExceptionHandler(RuntimeException e, HttpServletRequest request) {
        AppErrorDTO errorDTO = new AppErrorDTO(
                request.getRequestURI(),
                e.getMessage(),
                404
        );
        return ResponseEntity.status(404).body(errorDTO);
    }
}
