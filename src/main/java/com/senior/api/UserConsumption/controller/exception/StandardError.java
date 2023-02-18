package com.senior.api.UserConsumption.controller.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StandardError implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd hh:mm:ss")
    private LocalDateTime timeStamp;

    private HttpStatus statusHttp;
    private String message;

    public StandardError(HttpStatus status) {
        this();
        this.statusHttp = status;
    }

    public StandardError(HttpStatus status, String message) {
        this();
        this.statusHttp = status;
        this.message = message;
    }

    public StandardError() {
        timeStamp = LocalDateTime.now();
    }
}
