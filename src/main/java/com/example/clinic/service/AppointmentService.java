package com.example.clinic.service;

import com.example.clinic.Repository.AppointmentRepository;
import com.example.clinic.Repository.AvailabilityRepository;
import com.example.clinic.Repository.DoctorRepository;
import com.example.clinic.model.Appointment;

import com.example.clinic.model.Availability;
import com.example.clinic.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AppointmentService {


    @Autowired
    private AppointmentRepository appointmentRepository;

    public void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }



    }


