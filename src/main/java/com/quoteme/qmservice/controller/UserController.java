package com.quoteme.qmservice.controller;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.quoteme.qmservice.domain.User;
import com.quoteme.qmservice.dto.UserDto;
import com.quoteme.qmservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/current")
    public ResponseEntity<UserDto> getUser(Principal principal) {
        return userService.get(principal.getName())
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDto user) {
        if (StringUtils.isBlank(user.getPassword())) {
            return ResponseEntity.badRequest().body("Password cannot be empty");
        }
        if (userService.get(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("User with specified email already exists");
        }

        UserDto createdUser = userService.create(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable UUID userId, @RequestBody @Valid UserDto updatedUser) {
        Optional<User> existentUser = userService.get(userId);

        if (existentUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDto savedUser = userService.update(existentUser.get(), updatedUser);
        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/current")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(Principal principal) {
        userService.deleteUser(principal.getName());
    }

}
