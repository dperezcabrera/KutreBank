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
        return ResponseEntity.ok(userService.changePassword(auditor.getCurrentAuditor().get(), changePasswordDto));
    }

    @PostMapping("/change-password/{username}")
    @PreAuthorize("@roleChecker.isAdmin()")
    public ResponseEntity<MessageDto> postChangePassword(@PathVariable("username") String username, @RequestBody ChangePasswordDto changePasswordDto) {
        return ResponseEntity.ok(userService.changePassword(username, changePasswordDto));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable("id") long userId) {
        if (!roleChecker.isAdmin()) {
            featureService.checkFeature(Features.DATA_LEAK);
        }
        return userService.getById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    @PreAuthorize("@roleChecker.isAdmin()")
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/passwords")
    @PreAuthorize("@roleChecker.isAdmin()")
    public ResponseEntity<List<UserPasswordDto>> getPasswordAll() {
        return ResponseEntity.ok(userService.getPasswords());
    }
}
