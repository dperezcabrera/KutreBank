package com.github.dperezcabrera.bank.architecture.auth.dtos;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeatureDto {

    private String name;
    private LocalDateTime inactiveDate;
}
