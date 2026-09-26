package com.ampta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String errorCode;
}
