package com.rootsshivasou.contact.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rootsshivasou.contact.service.ContactService;
import com.rootsshivasou.moduleCommun.model.Contact;
import com.rootsshivasou.moduleCommun.model.dto.ContactDTO;

@RestController
@CrossOrigin(origins = "*") 
public class ContactController {
    
    @Autowired
    ContactService service;

    
    @GetMapping("/contacts")
    public List<ContactDTO> all() {
        List<ContactDTO> list = new ArrayList<ContactDTO>();
        for (Contact contact : service.getAllContacts()) {
            list.add(new ContactDTO(contact));
        }
        return list;
    }

    @GetMapping("/contact/{id}")
    public ContactDTO cat(@PathVariable("id") int identifier){
        return new ContactDTO(service.getContact(identifier));
    }

    /**
     * L'annotation requestbody permet de récurerer les infos écrits dans le corps
     * @param u
     * @return
     */
    @PostMapping("/contact")
    public ContactDTO insertContact(@RequestBody ContactDTO data) {
    Contact newContact = new Contact();
    newContact.setFirst_name(data.getFirst_name());
    newContact.setLast_name(data.getLast_name());
    newContact.setEmail(data.getEmail());
    newContact.setObject(data.getObject());
    newContact.setContent(data.getContent());
    

    return new ContactDTO(service.saveContact(newContact, data.getContent()));
}

    @DeleteMapping("/contact/{id}")
    public Boolean deleteContact(@PathVariable("id") int id) {
        Contact c = service.getContact(id);
        if (c != null) {
            service.deleteContact(id);
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}