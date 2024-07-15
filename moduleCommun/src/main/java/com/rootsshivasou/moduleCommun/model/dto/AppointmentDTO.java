package com.rootsshivasou.moduleCommun.model.dto;

import java.time.LocalDateTime;

import com.rootsshivasou.moduleCommun.model.Appointment;

import lombok.Data;

@Data
public class AppointmentDTO {

    private Integer id;
    private LocalDateTime date;
    private Boolean isValidated;
    private Integer userId;

    public AppointmentDTO () {

    }

    public AppointmentDTO (Appointment a) {
        this.id = a.getId();
        this.date = a.getDate();
        this.isValidated = a.getIsValidated();
        if (a.getUser() != null) {
            this.userId = a.getUser().getId();
        }

    }
    
}