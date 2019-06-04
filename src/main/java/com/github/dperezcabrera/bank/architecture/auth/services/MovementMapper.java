package com.github.dperezcabrera.bank.architecture.auth.services;

import com.github.dperezcabrera.bank.architecture.auth.entities.Movement;
import com.github.dperezcabrera.bank.architecture.auth.dtos.MovementDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MovementMapper {

	@Mappings({
		@Mapping(target = "originId", source = "origin.id"),
		@Mapping(target = "targetId", source = "target.id"),
		@Mapping(target = "originName", source = "origin.username"),
		@Mapping(target = "targetName", source = "target.username"),
		@Mapping(target = "date", source = "date", dateFormat = "yyyy/MM/dd HH:mm:ss")
	})
	MovementDto map(Movement movement);

	@Mapping(target = "date", source = "date", dateFormat = "yyyy/MM/dd HH:mm:ss")
	Movement map(MovementDto movement);
}
