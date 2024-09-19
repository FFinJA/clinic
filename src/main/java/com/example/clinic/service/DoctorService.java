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
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;


    public User getDoctorById(Long id) {

        return doctorRepository.findDoctorByUserId(id).orElse(null);
    }

    public User getDoctorByEmail(String email) {

        return doctorRepository.findDoctorByEmail(email);
    }


    public List<Appointment> getAppointmentsForDoctor(Long id) {

        List<Appointment> appointmentsForDoctor = appointmentRepository.findAppointmentsByDoctor_Id(id);

        return appointmentsForDoctor;
    }

    public List<User> getAllDoctors() {
        return doctorRepository.findAllDoctors();
    }




}