package com.rootsshivasou.creation.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rootsshivasou.creation.service.AppointmentService;
import com.rootsshivasou.moduleCommun.model.Appointment;
import com.rootsshivasou.moduleCommun.model.dto.AppointmentDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    AppointmentService service;

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    @GetMapping("/appointments")
    public List<AppointmentDTO> all() {
        List<AppointmentDTO> list = new ArrayList<>();
        for (Appointment appointment : service.getAllAppointments()) {
            list.add(new AppointmentDTO(appointment));
        }
        return list;
    }

    @GetMapping("/appointment/{id}")
    public AppointmentDTO app(@PathVariable("id") int id) {
        return new AppointmentDTO(service.getAppointmentById(id));
    }

    @GetMapping("/appointment/pending")
    public List<AppointmentDTO> getPendingAppointments() {
        List<AppointmentDTO> list = new ArrayList<>();
        for (Appointment appointment : service.getPendingAppointments()) {
            list.add(new AppointmentDTO(appointment));
        }
        return list;
    }

    @PostMapping("/appointment")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        logger.info("Received appointment creation request: {}", appointmentDTO);
        try {
            LocalDateTime dateTime = appointmentDTO.getDate();
            Appointment appointment = service.createAppointment(appointmentDTO.getUserId(), dateTime);
            AppointmentDTO responseDTO = new AppointmentDTO(appointment);
            logger.info("Appointment created successfully: {}", responseDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating appointment: ", e);
            return new ResponseEntity<>("Error creating appointment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/appointment/validate/{id}")
    public ResponseEntity<?> validateAppointment(@PathVariable int id) {
        try {
            Appointment appointment = service.validateAppointment(id);
            AppointmentDTO responseDTO = new AppointmentDTO(appointment);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Appointment not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
