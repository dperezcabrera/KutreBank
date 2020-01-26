package com.github.dperezcabrera.bank.architecture.auth.controllers;

import com.github.dperezcabrera.bank.architecture.auth.services.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/teams")
@AllArgsConstructor
public class TeamController {

    private UserService userService;

    @GetMapping
    public List<String> getAll() {
        return userService.getTeams();
    }
}
