package com.quoteme.qmservice.controller;

import com.quoteme.qmservice.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Mock
    UserService userService;

    UserController controller;

    @BeforeAll
    public void setup() {
        controller = new UserController(userService);
    }

    @Test
    public void getUser() {
    }

}
