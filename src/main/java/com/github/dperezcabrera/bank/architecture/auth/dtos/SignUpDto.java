package com.github.dperezcabrera.bank.architecture.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

    private String username;
    private String password;
    private String code;
}
