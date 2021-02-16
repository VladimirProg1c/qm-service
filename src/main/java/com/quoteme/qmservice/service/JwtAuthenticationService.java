package com.quoteme.qmservice.service;

import com.quoteme.qmservice.dto.Credentials;
import com.quoteme.qmservice.dto.JwtToken;
import com.quoteme.qmservice.dto.UserDto;
import com.quoteme.qmservice.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthenticationService {

    private AuthenticationManager authenticationManager;

    private JwtTokenUtil jwtTokenUtil;

    private UserDetailsService userDetailsService;

    private UserService userService;

    @Autowired
    public JwtAuthenticationService(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
                                    UserDetailsService userDetailsService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    public JwtToken createAuthenticationToken(Credentials credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.getEmail());
        String token = jwtTokenUtil.generateToken(userDetails);

        UserDto user = userService.get(credentials.getEmail()).get();
        userService.updateLastLoginDate(user.getId());

        return new JwtToken(token, user);
    }

}
