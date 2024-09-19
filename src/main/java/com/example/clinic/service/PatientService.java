package com.example.clinic.service;

import com.example.clinic.Repository.AppointmentRepository;
import com.example.clinic.Repository.DoctorRepository;
import com.example.clinic.model.User;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import com.example.clinic.Repository.PatientRepository;
import com.example.clinic.model.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Controller
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public User getPatientById(Long id) {

        return patientRepository.findPatientByUserId(id).orElse(null);
    }

    public List<Appointment> getAppointmentsForPatient(Long id) {

        List<Appointment> appointmentsForPatient = appointmentRepository.findByPatientId(id);

        return appointmentsForPatient;
    }


    public User getPatientByFirstNameAndLastName(String firstName, String lastName) {
        return patientRepository.findPatientByFirstNameAndLastName(firstName, lastName);
    }

    public User getPatientByEmail(String email) {
        return patientRepository.findPatientByEmail(email);
    }

    public void savePatient(User user) {
        patientRepository.save(user);
    }

    public long getPatientAppointmentsCount(long userId) {
        return appointmentRepository.countByPatient_Id(userId);
    }


}
