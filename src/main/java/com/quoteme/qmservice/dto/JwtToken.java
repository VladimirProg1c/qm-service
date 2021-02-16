package com.quoteme.qmservice.dto;

import lombok.Value;

@Value
public class JwtToken {

	String token;
	UserDto user;
}
