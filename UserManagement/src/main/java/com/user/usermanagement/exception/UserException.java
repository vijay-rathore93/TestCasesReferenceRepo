package com.user.usermanagement.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserException extends RuntimeException {

    private String errorMessage;

}
