package com.github.dperezcabrera.bank.architecture.auth.controllers;

import com.github.dperezcabrera.bank.architecture.auth.dtos.CredentialsDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.SignUpDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.UserDto;
import com.github.dperezcabrera.bank.architecture.auth.services.UserService;
import com.github.dperezcabrera.bank.architecture.common.ControllerUtility;
import com.github.dperezcabrera.bank.architecture.common.MessageDto;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final SessionRegistry sessionRegistry;

    private final UserService userService;

    private final AuditorAware<String> auditorAware;
    
    @GetMapping("/auth/login")
    public ResponseEntity<UserDto> login() {
        return auditorAware.getCurrentAuditor()
                .map(username -> userService.getUser(username)
                .map(ResponseEntity::ok)
                .orElseGet(ControllerUtility::forbidden))
                .orElseGet(ControllerUtility::forbidden);
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
            }).orElseGet(ControllerUtility::forbidden);
        } catch (BadCredentialsException ex) {
            log.error("Bad credentials error", ex);
            return ControllerUtility.forbidden();
        }
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<MessageDto> signUp(@RequestBody SignUpDto signUpDto) {
        return ResponseEntity.ok(userService.signUp(signUpDto));
    }
}
