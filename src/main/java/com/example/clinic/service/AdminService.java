package com.example.clinic.service;

import com.example.clinic.Repository.AppointmentRepository;
import com.example.clinic.Repository.AvailabilityRepository;
import com.example.clinic.Repository.AdminRepository;
import com.example.clinic.model.Appointment;

import com.example.clinic.model.Availability;
import com.example.clinic.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AvailabilityRepository availabilityRepository;


    public User getAdminById(Long id) {

        return adminRepository.findAdminByUserId(id);
    }


    public User getAdminByEmail(String email) {

        return adminRepository.findAdminByEmail(email);
    }


    public List<Appointment> getAppointments() {

        List<Appointment> allAppointments = appointmentRepository.findAll();

        return allAppointments;
    }

    public List<Availability> getAvailabilities() {

        List<Availability> allAvailabilities = availabilityRepository.findAll();

        return allAvailabilities;
    }

    public List<User> getAllDoctors() {
        return adminRepository.findAllDoctors();
    }

    public List<Availability> getAvailabilitiesByDoctor(Long doctorId) {
        return adminRepository.findByDoctorIdOrderByDayOfWeekAndStartTimeGeneral(doctorId);
    }

    public List<Availability> getAllAvailabilitiesGroupedGeneral() {
        return adminRepository.findAllGroupedByDoctorAndDayOfWeekGeneral();
    }

    public List<Availability> getAvailabilitiesByDoctorSpecific(Long doctorId) {
        return adminRepository.findByDoctorIdOrderByDayOfWeekAndStartTimeSpecific(doctorId);
    }

    public List<Availability> getAllAvailabilitiesGroupedSpecific() {
        return adminRepository.findAllGroupedByDoctorAndDayOfWeekSpecific();
    }

    public Optional<Availability> getSepcificAvailability(Long id, LocalDate date,Integer dayOfWeek,LocalTime startTime, LocalTime endTime) {
        return availabilityRepository.findSpecificAvailability(id,date,dayOfWeek,startTime,endTime);
    }

    @Transactional // 确保删除操作在事务中执行
    public void deleteAvailabilityByIdGeneral(Long id) {
        adminRepository.deleteByIdWithoutDate(id);
    }

    @Transactional // 确保删除操作在事务中执行
    public void deleteAvailabilityByIdGSpecific(Long id) {
        adminRepository.deleteByIdWithDate(id);
    }





}