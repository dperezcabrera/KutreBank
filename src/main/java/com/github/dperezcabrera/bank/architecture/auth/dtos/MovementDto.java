package com.github.dperezcabrera.bank.architecture.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovementDto {

	private Long id;
	private long amount;
	private String date;
	private String originName;
	private long originId;
	private String targetName;
	private long targetId;
	private String description;
}
