package com.andersen.TicketToRide.service.impl;

import com.andersen.TicketToRide.model.CustomUser;
import com.andersen.TicketToRide.repository.CustomUserRepository;
import com.andersen.TicketToRide.service.CustomUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserServiceImpl implements CustomUserService, UserDetailsService {

    private static final Logger log = LogManager.getLogger();
    @Autowired
    private CustomUserRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<CustomUser> user = repository.findByUsername(username);
        if (user.isPresent()){
            var userObject = user.get();
            return User.builder()
                    .username(userObject.getUsername())
                    .password(userObject.getPassword())
                    .build();
        } else {
            log.debug("User with username " + username + " is not found");
            throw new UsernameNotFoundException(username);
        }
    }

    public CustomUser saveCustomUser(CustomUser user){
        return repository.save(user);
    }
}
