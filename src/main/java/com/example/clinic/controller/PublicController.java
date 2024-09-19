package com.example.clinic.controller;


import com.example.clinic.Repository.UserRepository;
import com.example.clinic.model.Role;
import com.example.clinic.model.User;
import com.example.clinic.service.PatientService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Controller
public class PublicController {

    @Autowired
    private PatientService patientService;
    @Autowired
    private UserRepository userRepository;

    String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/uploads";


    @GetMapping({"/home","/","/index",""})
    public String viewLoginPage(Model model) {
        return "public-home";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login-form"; // 这将返回 src/main/resources/templates/login-form.html
    }

    @GetMapping("/register")
    public String viewRegisterPage(Model model){
        model.addAttribute("patient", new User());
        return "register-form";
    }

    @PostMapping("/register")
    public String processRegister(@Valid User user, BindingResult result, Model model){
        if (result.hasErrors()) {
            log.debug(String.valueOf(result));
            return "register-form";
        }

        if (!user.getPassword().equals(user.getPassword2())) {
            result.rejectValue("password2", "passwordsDoNotMatch", "Passwords must match");
            return "register-form";
        }

        if (patientService.getPatientByFirstNameAndLastName(user.getFirstName(),user.getLastName()) != null) {
            result.rejectValue("firstName", "usernameExists", "Username already exists");
            return "register-form";
        }

        if (patientService.getPatientByEmail(user.getEmail()) != null) {
            result.rejectValue("email", "emailExists", "Email already exists");
            return "register-form";
        }

        if(user.getRole() == null){
//            user.setRole(Role.PATIENT);
            user.setRole(Role.ADMIN);
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        user.setActive(true);

        patientService.savePatient(user);

        return "register-success";
    }

    @GetMapping("/edit")
    public String viewEditPage(Principal principal, RedirectAttributes redirectAttributes, Model model){
        if(principal == null){
            redirectAttributes.addFlashAttribute("message", "Please login first!");
            return("redirect:/login");
        }

        String email = principal.getName();
        User user = userRepository.findByEmail(email);
//        model.addAttribute("patient", user);

        String photo = user.getPhoto();
        if(photo == null || photo.isEmpty()) {
            photo = "/public/avatar.png";
        }
        model.addAttribute("imgUrl", photo);

        String viewAppsLink = "/patients/viewappointments/";
        String makeAppLinke = "/appointment";
        String patientId = user.getId().toString();
        viewAppsLink = viewAppsLink + patientId;
        makeAppLinke = "/patients/" + patientId + makeAppLinke;
        Map<String, String> controllerLinks = new LinkedHashMap<>();
        controllerLinks.put("View appointment(s)", viewAppsLink);
        controllerLinks.put("Make appointment", makeAppLinke);
        model.addAttribute("controllerLinks", controllerLinks);

        ArrayList<String> defaultLinks = new ArrayList<>();
        defaultLinks.add("Patients Home");
        defaultLinks.add("/patients/");
        String editInformationLink = "/edit";

        model.addAttribute("defaultLinks", defaultLinks);
        model.addAttribute("editInformationLink", editInformationLink);



        model.addAttribute("user", user);

        return "edit-form";
    }

    @PostMapping("/edit")
    public String processEdit(@RequestHeader(value = "referer", required = false) String referer,@Valid User user, BindingResult result, Principal principal, RedirectAttributes redirectAttributes, Model model){
        if(principal == null){
            redirectAttributes.addFlashAttribute("message", "Please login first!");
            return("redirect:/login");
        }

        String email = principal.getName();
        User userInDb = userRepository.findByEmail(email);

        String phone = user.getPhone();
        userInDb.setPhone(phone);
        String photo  = user.getPhoto();
        userInDb.setPhoto(photo);
        String gender = user.getGender();
        userInDb.setGender(gender);

        userRepository.save(userInDb);
        redirectAttributes.addFlashAttribute("message", "Information updated successfully!");
        return "redirect:/patients/";
    }



}
