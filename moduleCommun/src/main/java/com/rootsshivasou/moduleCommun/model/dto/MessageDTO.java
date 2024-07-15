package com.rootsshivasou.moduleCommun.model.dto;

import java.time.LocalDateTime;

import com.rootsshivasou.moduleCommun.model.Message;

import lombok.Data;

@Data
public class MessageDTO {

    private Integer id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer issuerId;
    private Integer recipientId;
    
    public MessageDTO () {
    
    }

    public MessageDTO (Message m) {
        this.id = m.getId();
        this.content = m.getContent();
        this.createdAt = m.getCreatedAt();
        this.updatedAt = m.getCreatedAt();
        if ( m.getUser() != null ) {
            this.issuerId = m.getUser().getId();
        }
        this.recipientId = m.getRecipientId();
    }

    public Integer getIssuerId() {
        return issuerId;
    }
    
}
