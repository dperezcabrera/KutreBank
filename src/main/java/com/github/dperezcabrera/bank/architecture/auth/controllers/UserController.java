package com.github.dperezcabrera.bank.architecture.auth.controllers;

import com.github.dperezcabrera.bank.architecture.auth.dtos.Features;
import com.github.dperezcabrera.bank.architecture.auth.dtos.UserDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.UserPasswordDto;
import com.github.dperezcabrera.bank.architecture.auth.services.FeatureService;
import com.github.dperezcabrera.bank.architecture.auth.services.UserService;
import com.github.dperezcabrera.bank.architecture.security.RoleChecker;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

	private UserService userService;
	private FeatureService featureService;
	private RoleChecker roleChecker;

	@GetMapping
	public ResponseEntity<List<UserDto>> getAll() {
		if (featureService.isActive(Features.DATA_LEAK) || roleChecker.isAdmin()) {
			return ResponseEntity.ok(userService.getAll());
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}

	@GetMapping("/passwords")
	@PreAuthorize("@roleChecker.isAdmin()")
	public ResponseEntity<List<UserPasswordDto>> getPasswordAll() {
		if (featureService.isActive(Features.DATA_LEAK) || roleChecker.isAdmin()) {
			return ResponseEntity.ok(userService.getPasswords());
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}
	
	@PutMapping("/locked/{id}/{locked}")
	@PreAuthorize("@roleChecker.isAdmin()")
	public ResponseEntity<UserDto> setLock(@PathVariable("id") long userId, @PathVariable("locked") boolean locked) {
		return userService.setUserLock(userId, locked)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
}