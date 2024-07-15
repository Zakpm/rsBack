package com.rootsshivasou.creation.service.email;

public class TokenInvalidException extends RuntimeException {

    public TokenInvalidException() {
        
    }
    public TokenInvalidException(String message) {
        super(message);
    }
    
}