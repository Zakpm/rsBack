package com.rootsshivasou.moduleCommun.model.dto;

import java.time.LocalDateTime;

import com.rootsshivasou.moduleCommun.model.Contact;

import lombok.Data;

@Data
public class ContactDTO {

    private Integer id;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private String object;
    private String content;
    private LocalDateTime created_at;

    public ContactDTO () {
        
    }

    public ContactDTO (Contact c) {
        this.id = c.getId();
        this.first_name = c.getFirst_name();
        this.last_name = c.getLast_name();
        this.email = c.getEmail();
        this.object = c.getObject();
        this.content = c.getContent();
        if (c.getPhone()!= null) {
            this.phone = c.getPhone();
        }
        this.created_at = c.getCreated_at();
    }

    

    
}