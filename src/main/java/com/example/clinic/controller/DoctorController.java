package com.example.clinic.controller;

import com.example.clinic.model.Appointment;

import com.example.clinic.model.User;
import com.example.clinic.service.DoctorService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/{id}/appointments")
    public String viewDoctorAppointments(@PathVariable Long id, Model model) {
        List<Appointment> appointments = doctorService.getAppointmentsForDoctor(id);
        model.addAttribute("appointments", appointments);
        return "doctor-appointments";
    }

    @GetMapping({"/", "/index", "/?continue",""})
    public String viewDoctorHomepage(Principal principal, Model model){
        String email = principal.getName();
        User Doctor = doctorService.getDoctorByEmail(email);
        String toAppLink = "/appointments";
        String doctorId = Doctor.getId().toString();
        toAppLink = "/doctors/" + doctorId + toAppLink;
        model.addAttribute("doctor", Doctor);
        model.addAttribute("toAppLink", toAppLink);
        return "doctor-homepage";
    }





}
