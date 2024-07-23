package com.andersen.TicketToRide.controller;

import com.andersen.TicketToRide.model.CustomUser;
import com.andersen.TicketToRide.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    @Autowired
    CustomUserService customUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register/traveller")
    public CustomUser createTraveller(@RequestBody CustomUser traveller){
        traveller.setRole("TRAVELLER");
        traveller.setPassword(passwordEncoder.encode(traveller.getPassword()));
        return  customUserService.saveCustomUser(traveller);
    }

}
