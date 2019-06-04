package com.github.dperezcabrera.bank.architecture.auth.services;

import com.github.dperezcabrera.bank.architecture.auth.entities.Code;
import com.github.dperezcabrera.bank.architecture.auth.dtos.CodeDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CodeMapper {

    CodeDto map(Code code);

    Code map(CodeDto code);
}
