package com.rootsshivasou.contact.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rootsshivasou.moduleCommun.model.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {
    List<Message> findByUserIdOrderByCreatedAtDesc(Integer userId);
    
}