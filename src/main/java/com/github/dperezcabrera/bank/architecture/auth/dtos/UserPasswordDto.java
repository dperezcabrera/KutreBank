package com.github.dperezcabrera.bank.architecture.auth.dtos;

import lombok.Data;

@Data
public class UserPasswordDto {

    private Long id;

    private String password;

    private String username;

    private long amount;

    private boolean locked;
}
