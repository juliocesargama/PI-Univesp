package com.univesp.PI1.utils.handler;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CustomException extends RuntimeException{
    private HttpStatus code;
    private String message;


    public CustomException(HttpStatus code, String message) {
        this.code = code;
        this.message = message;

    }
}
