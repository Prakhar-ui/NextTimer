package com.prakhar.nextTimer.Exception;

public class CustomAuthorizationException extends RuntimeException {
    public CustomAuthorizationException(String message) {
        super(message);
    }
}