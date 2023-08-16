package com.example.demo.exception;

import com.example.demo.dto.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.security.access.AccessDeniedException;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse handleException(HttpServletRequest request, Exception exception ){
        return  BaseResponse.builder().code(10001L).message(exception.getMessage()).build();
    }
    // bu çalışıyo
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse> handleException(HttpServletRequest request, IllegalArgumentException exception ){
        return  new ResponseEntity<>(new BaseResponse<>(100002L,exception.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse> handleException(HttpServletRequest request, AccessDeniedException exception ){
        return  new ResponseEntity<>(new BaseResponse<>(100003L,exception.getMessage()),HttpStatus.FORBIDDEN);
    }

}
