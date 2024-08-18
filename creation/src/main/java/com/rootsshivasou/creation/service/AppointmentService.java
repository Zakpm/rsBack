package com.rootsshivasou.creation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rootsshivasou.creation.repository.AppointmentRepository;
import com.rootsshivasou.creation.repository.UserRepository;
import com.rootsshivasou.creation.service.email.EmailService;
import com.rootsshivasou.moduleCommun.model.Appointment;
import com.rootsshivasou.moduleCommun.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    public List<Appointment> getAllAppointments() {
        return (List<Appointment>) appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(int id) {
        Optional<Appointment> optional = appointmentRepository.findById(id);
        return optional.isPresent() ? optional.get() : null;
    }

    public Appointment createAppointment(int userId, LocalDateTime date, String message) {
        logger.info("Creating appointment for user ID: {} at date: {}", userId, date);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setDate(date);
        appointment.setMessage("Votre demande de rendez-vous a été prise en compte.");
        Appointment savedAppointment = appointmentRepository.save(appointment);
        logger.info("Appointment created successfully: {}", savedAppointment);
        return savedAppointment;
    }

    public List<Appointment> getPendingAppointments() {
        return appointmentRepository.findByIsValidatedFalse();
    }

    public Appointment validateAppointment(int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseT