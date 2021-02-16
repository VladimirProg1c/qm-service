package com.quoteme.qmservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    UUID id;

    @NotEmpty(message = "first name cannot be empty")
    @Size(min = 2, max = 100, message = "first name size must be between 2 and 100 chars")
    String firstName;

    @NotEmpty(message = "second name cannot be empty")
    @Size(min = 2, max = 100, message = "second name size must be between 2 and 100 chars")
    String secondName;

    @NotEmpty(message = "email name cannot be empty")
    @Email(message = "email has an invalid format")
    String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 6, max = 255, message = "password size must be between 6 and 255 chars")
    String password;

}
