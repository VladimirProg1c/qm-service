package com.quoteme.qmservice.service;

import com.quoteme.qmservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmailAndDeletedFalse(email)
                .map(user -> new org.springframework.security.core.userdetails.User(email, user.getPassword(), List.of()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

}
