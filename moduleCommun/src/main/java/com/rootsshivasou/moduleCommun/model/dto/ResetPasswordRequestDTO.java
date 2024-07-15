package com.rootsshivasou.moduleCommun.model.dto;

import java.time.LocalDateTime;

import com.rootsshivasou.moduleCommun.model.ResetPasswordRequest;
import com.rootsshivasou.moduleCommun.model.User;

import lombok.Data;

@Data
public class ResetPasswordRequestDTO {

    private Number id;
    private User userRequest;
    private String token;
    private LocalDateTime expires_at;
    private Boolean isUsed;

    public ResetPasswordRequestDTO () {

    }

    public ResetPasswordRequestDTO (ResetPasswordRequest r) {
        this.id = r.getId();
        this.userRequest = r.getUserRequest();
        this.token = r.getToken();
        this.expires_at = r.getExpiresAt();
        this.isUsed = r.getIsUsed();

    }

    
}