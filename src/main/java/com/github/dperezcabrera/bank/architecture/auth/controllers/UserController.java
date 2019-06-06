package com.github.dperezcabrera.bank.architecture.auth.controllers;

import com.github.dperezcabrera.bank.architecture.auth.dtos.ChangePasswordDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.Features;
import com.github.dperezcabrera.bank.architecture.auth.dtos.UserDto;
import com.github.dperezcabrera.bank.architecture.auth.dtos.UserPasswordDto;
import com.github.dperezcabrera.bank.architecture.auth.services.FeatureService;
import com.github.dperezcabrera.bank.architecture.auth.services.UserService;
import com.github.dperezcabrera.bank.architecture.common.MessageDto;
import com.github.dperezcabrera.bank.architecture.security.RoleChecker;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private FeatureService featureService;
    private RoleChecker roleChecker;
    private AuditorAware<String> auditor;

    @PostMapping("/change-password")
    public ResponseEntity<MessageDto> postChangePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        return userService.changePassword(auditor.getCurrentAuditor().get(), changePasswordDto, false).toResponse();
    }

    @PostMapping("/change-password/{username}")
    @PreAuthorize("@roleChecker.isAdmin()")
    public ResponseEntity<MessageDto> postChangePassword(@PathVariable("username") String username, @RequestBody ChangePasswordDto changePasswordDto) {
        return userService.changePassword(username, changePasswordDto, true).toResponse();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable("id") long userId) {
        if (featureService.isActive(Features.DATA_LEAK) || roleChecker.isAdmin()) {
            return userService.getById(userId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping
    @PreAuthorize("@roleChecker.isAdmin()")
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(userService.getAll());
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
