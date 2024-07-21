package com.andersen.TicketToRide.service;

import com.andersen.TicketToRide.model.CustomUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    CustomUser saveCustomUser(CustomUser traveller);
}
