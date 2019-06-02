package com.github.dperezcabrera.bank.architecture.auth.controllers;

import com.github.dperezcabrera.bank.architecture.auth.dtos.CredentialsDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.SignUpDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.UserDto;
import com.github.dperezcabrera.bank.architecture.auth.services.UserService;
import com.github.dperezcabrera.bank.architecture.common.MessageDto;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@RestController
@AllArgsConstructor
public class AuthenticationController {

	private static final ResponseEntity<?> FORBIDDEN_RESPONSE = ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
	
	private final AuthenticationManager authenticationManager;

	private final SessionRegistry sessionRegistry;

	private final UserService userService;

	private final AuditorAware<String> auditorAware;

	private static <T> ResponseEntity<T> forbidden() {
		return (ResponseEntity<T>) FORBIDDEN_RESPONSE;
	}

	@GetMapping("/auth/login")
	public ResponseEntity<UserDto> login() {
		return auditorAware.getCurrentAuditor()
				.map(username -> userService.getUser(username)
				.map(ResponseEntity::ok)
				.orElseGet(AuthenticationController::forbidden))
				.orElseGet(AuthenticationController::forbidden);
	}

	@PostMapping("/auth/login")
	public ResponseEntity<UserDto> login(HttpServletRequest request, @RequestBody CredentialsDto credentialsDto) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(credentialsDto.getUsername(), credentialsDto.getPassword());
		try {
			return userService.getUser(credentialsDto.getUsername()).map(userDto -> {
				Authentication auth = authenticationManager.authenticate(token);
				SecurityContextHolder.getContext().setAuthentication(auth);
				sessionRegistry.registerNewSession(request.getSession().getId(), auth.getPrincipal());
				return ResponseEntity.ok(userDto);
			}).orElseGet(AuthenticationController::forbidden);
		} catch (BadCredentialsException ex) {
			log.error("Bad credentials error", ex);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
	}

	@PostMapping("/auth/signup")
	public ResponseEntity<MessageDto> signUp(@RequestBody SignUpDto signUpDto) {
		try {
			userService.signUp(signUpDto);
			return ResponseEntity.ok(new MessageDto("El usuario ha sido registrado correctamente"));
		} catch (IllegalArgumentException ex) {
			log.error("No se ha podido registrar la peticion: " + signUpDto, ex);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageDto("No se ha podido registrar el usuario: " + ex.getMessage()));
		} catch (Exception ex) {
			log.error("No se ha podido registrar la peticion: " + signUpDto, ex);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageDto("No se ha podido registrar el usuario"));
		}
	}
}
