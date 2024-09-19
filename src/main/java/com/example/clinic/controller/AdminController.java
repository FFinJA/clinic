package com.example.clinic.controller;


import com.example.clinic.Repository.AppointmentRepository;
import com.example.clinic.Repository.UserRepository;
import com.example.clinic.service.AdminService;
import com.example.clinic.model.Appointment;
import com.example.clinic.model.Availability;
import com.example.clinic.model.User;
import com.example.clinic.service.AvailabilityService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.clinic.service.DoctorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminService adminService;
    @Autowired
    private AvailabilityService availabilityService;

    @ModelAttribute
    public void addCommonAttribute(Principal principal,Model model) {

        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        model.addAttribute("admin", user);

        String photo = user.getPhoto();
        if(photo == null || photo.isEmpty()) {
            photo = "/public/avatar.png";
        }
        model.addAttribute("imgUrl", photo);

        String viewStatisticsLink = "/admins/statistics";
        String viewEmailLink = "/admins/emails";
        String viewAvailabilitiesLink = "/admins/availabilities";
        String viewAppoinetmentsLink = "/admins/appointments";
        Map<String, String> controllerLinks = new LinkedHashMap<>();
        controllerLinks.put("Statistics", viewStatisticsLink);
        controllerLinks.put("Email setting",viewEmailLink);
        controllerLinks.put("Availabilities",viewAvailabilitiesLink);
        controllerLinks.put("Apoinetments",viewAppoinetmentsLink);
        model.addAttribute("controllerLinks", controllerLinks);

        ArrayList<String> defaultLinks = new ArrayList<>();
        defaultLinks.add("Admin Home");
        defaultLinks.add("/admins/");
        String editInformationLink = "/edit";

        model.addAttribute("defaultLinks", defaultLinks);
        model.addAttribute("editInformationLink", editInformationLink);
    }


    @GetMapping({"/", "/index", "/?continue",""})
    public String index(Principal principal, Model model) {

        return "admin-home";
    }

    @GetMapping("/appointments")
    public String viewAdminAppointments(Model model) {
        List<Appointment> appointments = adminService.getAppointments();
        model.addAttribute("appointments", appointments);
        return "admin-appointments";
    }


    @GetMapping("/register")
    public String viewRegisterPage(Model model){
        model.addAttribute("doctor", new User());
        return "register-form";
    }

    @PostMapping("/register")
    public String processRegisterForm(@Valid User user, BindingResult result, Model model){
        return null;
    }

    @GetMapping("/statistics")
    public String viewStatistics(Model model){
        return "statistics";
    }

    @GetMapping("/statistics/appointments")
    public String viewAppointments(Model model){
        return null;
    }

    @GetMapping("/statistics/doctors")
    public String viewDoctors(Model model){
        return null;
    }

    @GetMapping("/statistics/patients")
//    including charts
    public String viewPatients(Model model){
        return null;
    }

    @GetMapping("/emails")
    public String viewEmail(Model model){
        return null;
    }

    @GetMapping("/emails/template")
    public String viewEmailTemplate(Model model){
        return null;
    }

    @GetMapping("/availabilities")
    public String viewAvailability(Principal principal,Model model){

        String email = principal.getName();
        User adminUser = userRepository.findByEmail(email);
        model.addAttribute("adminUser", adminUser);
        List<User> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);

        List<Availability> generalAvailabilities = adminService.getAllAvailabilitiesGroupedGeneral();
        if(!generalAvailabilities.isEmpty()) {
            Map<User, List<Availability>> availabilitiesGroupedByDoctorGeneral = generalAvailabilities.stream()
                    .collect(Collectors.groupingBy(Availability::getDoctor));
            model.addAttribute("availabilitiesGroupedByDoctorGeneral", availabilitiesGroupedByDoctorGeneral);
        }

        List<Availability> specificAvailable = adminService.getAllAvailabilitiesGroupedSpecific();
        if(!specificAvailable.isEmpty()){
            Map<User, List<Availability>> availabilitiesGroupedByDoctorSpecific = specificAvailable.stream()
                    .collect(Collectors.groupingBy(Availability::getDoctor));
            model.addAttribute("availabilitiesGroupedByDoctorSpecific", availabilitiesGroupedByDoctorSpecific);
        }
        return "admin-availabilities";
    }

    @GetMapping("/availabilities/newgeneral")
    public String showAvailabilityForm(Model model){
        Availability availability = new Availability();
        List<User> doctors = doctorService.getAllDoctors();
        model.addAttribute("availability", availability);
        model.addAttribute("doctors", doctors);

        return "admin-newgeneralavailability";
    }

    @GetMapping("/availabilities/newspecific")
    public String showSpecificAvailabilityForm(Model model){
        Availability availability = new Availability();
        List<User> doctors = doctorService.getAllDoctors();
        model.addAttribute("availability", availability);
        model.addAttribute("doctors", doctors);

        return "admin-newspecificavailability";
    }

    @GetMapping("/availabilities/maintenance")
    public String viewAvailabilityMaintenance(Model model){
        return null;
    }

    @PostMapping("/availabilities/savegeneral")
    public String processGeneralAvailability(@Valid Availability availability, BindingResult result,RedirectAttributes redirectAttributes, Model model){
        if(result.hasErrors()){
            model.addAttribute("availability", availability);
            return "admin-newgeneralavailability";
        }

        Long doctorId = availability.getDoctor().getId();
        LocalDate date = availability.getDate();
        Integer dayOfWeek = availability.getDayOfWeek();
        LocalTime startTime = availability.getStartTime();
        LocalTime endTime = availability.getEndTime();
        Optional<Availability> specificAvailability = adminService.getSepcificAvailability(doctorId,date,dayOfWeek,startTime,endTime);
        if(specificAvailability.isEmpty())
        {
            availabilityService.saveAvailability(availability);
            redirectAttributes.addFlashAttribute("message", "Added a new schedule rule(general)!");
        }
        return "redirect:/admins/availabilities";
    }

    @PostMapping("/availabilities/savespecific")
    public String processsaceSpecificAvailability(@Valid Availability availability, BindingResult result,RedirectAttributes redirectAttributes, Model model){
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("errorMessage", "There were errors in your form submission. Please fix them and try again.");
            model.addAttribute("availability", availability);
            return "admin-newspecificavailability";
        }

        Long doctorId = availability.getDoctor().getId();
        LocalDate date = availability.getDate();
        Integer dayOfWeek = availability.getDayOfWeek();
        LocalTime startTime = availability.getStartTime();
        LocalTime endTime = availability.getEndTime();
        Optional<Availability> specificAvailability = adminService.getSepcificAvailability(doctorId,date,dayOfWeek,startTime,endTime);
        if(specificAvailability.isEmpty())
        {
            availabilityService.saveAvailability(availability);
            redirectAttributes.addFlashAttribute("message", "Added a new schedule rule(specific)!");
        }
        return "redirect:/admins/availabilities";
    }



    @GetMapping("/availabilities/deletegeneral/{id}")
    public String processAvailabilityDelete(@PathVariable Long id,RedirectAttributes redirectAttributes,Model model){

        adminService.deleteAvailabilityByIdGeneral(id);
        redirectAttributes.addFlashAttribute("message", "Availability deleted successfully!");

        return "redirect:/admins/availabilities";
    }

}
