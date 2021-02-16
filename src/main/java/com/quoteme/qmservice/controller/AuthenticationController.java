package com.quoteme.qmservice.controller;

import com.quoteme.qmservice.dto.Credentials;
import com.quoteme.qmservice.dto.JwtToken;
import com.quoteme.qmservice.service.JwtAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@CrossOrigin
public class AuthenticationController {

	private JwtAuthenticationService authenticationService;

	@Autowired
	public AuthenticationController(JwtAuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@PostMapping(value = "/authenticate")
	public ResponseEntity<JwtToken> authenticate(@Valid @RequestBody Credentials credentials)
			throws Exception {
		JwtToken jwtToken = authenticationService.createAuthenticationToken(credentials);
		return ResponseEntity.ok(jwtToken);
	}

}
