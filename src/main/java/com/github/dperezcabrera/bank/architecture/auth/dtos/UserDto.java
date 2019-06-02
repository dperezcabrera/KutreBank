package com.github.dperezcabrera.bank.architecture.auth.dtos;

import lombok.Data;

@Data
public class UserDto {

	private Long id;

	private String username;

	private long amount;
}
