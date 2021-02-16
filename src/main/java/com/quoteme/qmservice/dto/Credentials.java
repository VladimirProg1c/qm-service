package com.quoteme.qmservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class Credentials {

	@NotEmpty(message = "email cannot be empty")
	String email;

	@NotEmpty(message = "password cannot be empty")
	String password;

}
