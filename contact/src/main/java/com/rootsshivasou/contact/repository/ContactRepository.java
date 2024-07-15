package com.rootsshivasou.contact.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rootsshivasou.moduleCommun.model.Contact;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Integer> {
    
}