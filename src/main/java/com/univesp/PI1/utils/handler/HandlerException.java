package com.univesp.PI1.utils.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerException {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<EndpointResponse> handleCustomException(CustomException e) {
        return ResponseEntity.status(e.getCode()).body(new EndpointResponse(e.getMessage()));
    }
}
