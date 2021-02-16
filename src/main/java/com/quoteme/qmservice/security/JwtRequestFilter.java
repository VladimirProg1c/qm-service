package com.quoteme.qmservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private static final String AUTH_HEADER = "Authorization";
	private static final String AUTH_HEADER_PREFIX = "Bearer ";

	private UserDetailsService userService;
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	public JwtRequestFilter(UserDetailsService userService, JwtTokenUtil jwtTokenUtil) {
		this.userService = userService;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		Optional<String> jwtToken = getToken(request);
		Optional<String> userEmail = extractUserEmail(jwtToken);

		if (userEmail.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
			addUserToSecurityContext(request, jwtToken.get(), userEmail.get());
		}

		chain.doFilter(request, response);
	}

	private void addUserToSecurityContext(HttpServletRequest request, String jwtToken, String userEmail) {
		UserDetails userDetails = this.userService.loadUserByUsername(userEmail);

		if (jwtTokenUtil.isTokenValid(jwtToken, userDetails)) {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			authenticationToken
					.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}
	}

	private Optional<String> extractUserEmail(Optional<String> jwtToken) {
		return jwtToken.map(token -> jwtTokenUtil.getUserEmailFromToken(token));
	}

	private Optional<String> getToken(HttpServletRequest request) {
		String requestTokenHeader = request.getHeader(AUTH_HEADER);

		if (requestTokenHeader == null || !requestTokenHeader.startsWith(AUTH_HEADER_PREFIX)) {
			return Optional.empty();
		}

		String token = requestTokenHeader.substring(AUTH_HEADER_PREFIX.length());

		if (jwtTokenUtil.isTokenExpired(token)) {
			throw new AccessDeniedException("token has expired");
		}

		return Optional.of(token);
	}

}
