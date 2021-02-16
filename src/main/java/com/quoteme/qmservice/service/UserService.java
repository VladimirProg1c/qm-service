package com.quoteme.qmservice.service;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.quoteme.qmservice.domain.User;
import com.quoteme.qmservice.dto.UserDto;
import com.quoteme.qmservice.mapper.UserMapper;
import com.quoteme.qmservice.repository.UserRepository;
import com.quoteme.qmservice.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;

    private UserMapper userMapper;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> get(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<UserDto> get(String email) {
        return userRepository
                .findUserByEmailAndDeletedFalse(email)
                .map(userMapper::mapToUserDto);
    }

    public UserDto create(UserDto user) {
        User newUser = userMapper.mapToUser(user);
        newUser.setId(UUID.randomUUID());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setCreationDate(DateUtils.getCurrentTime());

        User createdUser = userRepository.save(newUser);
        return userMapper.mapToUserDto(createdUser);
    }

    public UserDto update(User existentUser, UserDto updatedUser) {
        checkEmailUniqueness(existentUser, updatedUser);

        User user = userMapper.mapToUser(updatedUser);
        setUserFields(user, existentUser);

        User savedUser = userRepository.save(user);
        return userMapper.mapToUserDto(savedUser);
    }

    private void setUserFields(User user, User existentUser) {
        user.setId(existentUser.getId());
        user.setLastLoginDate(existentUser.getLastLoginDate());
        user.setCreationDate(existentUser.getCreationDate());
        user.setLastModificationDate(DateUtils.getCurrentTime());

        if (StringUtils.isNotBlank(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(existentUser.getPassword());
        }
    }

    private void checkEmailUniqueness(User existentUser, UserDto updatedUser) {
        if (!Objects.equals(existentUser.getEmail(), updatedUser.getEmail())) {
            Optional<User> anotherUserWithEmail = userRepository.findUserByEmailAndDeletedFalse(updatedUser.getEmail());

            if (anotherUserWithEmail.isPresent()) {
                throw new IllegalArgumentException("New email is already used");
            }
        }
    }

    @Transactional
    public void updateLastLoginDate(UUID id) {
        userRepository.updateLastLoginDate(id, DateUtils.getCurrentTime());
    }

    @Transactional
    public void deleteUser(String email) {
        userRepository.delete(email);
    }

}
