package com.github.dperezcabrera.bank.architecture.auth.dtos;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "password")
public class SignUpDto {

	private String username;
	private String password;
	private String code;
}
